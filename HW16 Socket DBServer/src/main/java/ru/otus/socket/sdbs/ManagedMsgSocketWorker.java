package ru.otus.socket.sdbs;

import ru.otus.socket.sms.channel.SocketMsgWorker;

import java.io.IOException;
import java.net.Socket;

class ManagedMsgSocketWorker extends SocketMsgWorker {

    private final Socket socket;

    ManagedMsgSocketWorker(String host, int port) throws IOException {
        this(new Socket(host, port));
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
