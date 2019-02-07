package ru.otus.socket.sms.server;

public interface SocketMsgServer {
    boolean getRunning();

    void setRunning(boolean running);

    void start() throws Exception;
}
