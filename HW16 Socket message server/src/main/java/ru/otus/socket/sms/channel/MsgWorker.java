package ru.otus.socket.sms.channel;

import ru.otus.socket.sms.messagesystem.Message;

import java.io.Closeable;
import java.io.IOException;

public interface MsgWorker extends Closeable {
    void send(Message msg);

    Message pool();

    Message take() throws InterruptedException;

    void close() throws IOException;
}
