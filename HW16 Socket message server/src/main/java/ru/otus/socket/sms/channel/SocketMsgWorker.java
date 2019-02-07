package ru.otus.socket.sms.channel;

import com.google.gson.Gson;
import ru.otus.socket.sms.messagesystem.Address;
import ru.otus.socket.sms.messagesystem.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketMsgWorker implements MsgWorker {
    private static final Logger logger = Logger.getLogger(SocketMsgWorker.class.getName());
    private static final int WORKERS_COUNT = 2;

    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket socket;
    private final List<Runnable> shutdownRegistrations;

    public SocketMsgWorker(Socket socket) {
        this.socket = socket;
        this.shutdownRegistrations = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
    }

    @Override
    public void send(Message msg) {
        output.add(msg);
    }

    @Override
    public Message pool() {
        return input.poll();
    }

    @Override
    public Message take() throws InterruptedException {
        return input.take();
    }

    @Override
    public void close() {
        shutdownRegistrations.forEach(Runnable::run);
        shutdownRegistrations.clear();

        executor.shutdown();
    }

    public void init(Address address) {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
        send(new Message(new Address("init"),address,""));
    }

    public void addShutdownRegistration(Runnable runnable) {
        this.shutdownRegistrations.add(runnable);
    }

    //@Blocks
    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                //System.out.println("Message received: " + inputLine);
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) { //empty line is the end of the message
                    String json = stringBuilder.toString();
                    if (json.isEmpty())
                        continue;
                    Message msg = new Gson().fromJson(json, Message.class);
                    if (msg != null)
                        input.add(msg);
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "ReceiveMessage: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {
            while (socket.isConnected()) {
                Message msg = output.take(); //blocks
                String json = new Gson().toJson(msg);
                out.println(json);
                out.println(); //end of the message
            }
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, "SendMessage:" + e.getMessage());
        }
    }
}
