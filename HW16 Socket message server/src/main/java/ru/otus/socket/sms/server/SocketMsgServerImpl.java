package ru.otus.socket.sms.server;

import com.google.gson.Gson;
import ru.otus.socket.sms.messagesystem.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tully.
 */
public class SocketMsgServerImpl implements SocketMsgServer {
    private static final Logger logger = Logger.getLogger(SocketMsgServerImpl.class.getName());

    private static final int THREADS_NUMBER = 1;
    private static final int PORT = 5050;
    private static final int ECHO_DELAY = 100;
    private static final int CAPACITY = 256;
    private static final String MESSAGES_SEPARATOR = "\n\n";

    private final ExecutorService executor;
    private final Map<String, ChannelMessages> channelMessages;
    private final Map<String, String> adressee;

    public SocketMsgServerImpl() {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        channelMessages = new ConcurrentHashMap<>();
        adressee = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void start() throws Exception {
        executor.submit(this::echo);

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));

            serverSocketChannel.configureBlocking(false); //non blocking mode
            int ops = SelectionKey.OP_ACCEPT;
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, ops, null);

            logger.info("Started on port: " + PORT);

            while (true) {
                selector.select();//blocks
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    try {
                        if (key.isAcceptable()) {
                            SocketChannel channel = serverSocketChannel.accept(); //non blocking accept
                            String remoteAddress = channel.getRemoteAddress().toString();
                            System.out.println("Connection Accepted: " + remoteAddress);

                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);

                            channelMessages.put(remoteAddress, new ChannelMessages(channel));

                        } else if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();

                            ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
                            int read = channel.read(buffer);
                            if (read != -1) {
                                String result = new String(buffer.array()).trim();
                                System.out.println("Message received: " + result + " from: " + channel.getRemoteAddress());
                                Message msg = new Gson().fromJson(result, Message.class);
                                if (msg.getFrom().getId().equals("init")) {
                                    adressee.put(msg.getTo().getId(),channel.getRemoteAddress().toString());
                                } else {
                                    try {
                                        channelMessages.get(adressee.get(msg.getTo().getId())).messages.add(result);
                                    } catch (Exception e) {
                                        channelMessages.get(adressee.get(msg.getFrom().getId())).messages.add(result);
                                        logger.log(Level.SEVERE, "Нет адресата с таким именем");
                                    }

                                }
                            } else {
                                key.cancel();
                                String remoteAddress = channel.getRemoteAddress().toString();
                                channelMessages.remove(remoteAddress);
                                System.out.println("Connection closed, key canceled");
                            }
                        }
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, e.getMessage());
                        key.cancel();
                    } finally {
                        iterator.remove();
                    }
                }
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private Object echo() throws InterruptedException {
        while (true) {
            for (Map.Entry<String, ChannelMessages> entry : channelMessages.entrySet()) {
                ChannelMessages channelMessages = entry.getValue();
                if (channelMessages.channel.isConnected()) {
                    channelMessages.messages.forEach(message -> {
                        try {
                            System.out.println("Echoing message to: " + entry.getKey());
                            ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
                            buffer.put(message.getBytes());
                            buffer.put(MESSAGES_SEPARATOR.getBytes());
                            buffer.flip();
                            while (buffer.hasRemaining()) {
                                channelMessages.channel.write(buffer);
                            }
                        } catch (IOException e) {
                            logger.log(Level.SEVERE, e.getMessage());
                        }
                    });
                    channelMessages.messages.clear();
                }
            }
            Thread.sleep(ECHO_DELAY);
        }
    }

    @Override
    public boolean getRunning() {
        return true;
    }

    @Override
    public void setRunning(boolean running) {
        if (!running) {
            executor.shutdown();
            logger.info("Bye.");
        }
    }

    private class ChannelMessages {
        private final SocketChannel channel;
        private final List<String> messages = new ArrayList<>();

        private ChannelMessages(SocketChannel channel) {
            this.channel = channel;
        }
    }
}
