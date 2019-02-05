package ru.otus.socket.sms.server;

/**
 * Created by tully.
 */
public interface SocketMsgServer {
    boolean getRunning();

    void setRunning(boolean running);

    void start() throws Exception;
}
