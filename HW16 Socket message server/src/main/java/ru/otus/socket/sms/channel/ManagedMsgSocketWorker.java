package ru.otus.socket.sms.channel;

import java.io.IOException;
import java.net.Socket;

public class ManagedMsgSocketWorker extends SocketMsgWorker {

    private final Socket socket;

    public ManagedMsgSocketWorker(String host, int port) throws IOException {
        this(new Socket(host, port));
        if (socket.isConnected()) System.out.println("connection established successfully");
    }

    private ManagedMsgSocketWorker(Socket socket) {
        super(socket);
        this.socket = socket;
    }

    public void close() {
        super.close();
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
