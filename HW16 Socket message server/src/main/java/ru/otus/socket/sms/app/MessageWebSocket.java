package ru.otus.socket.sms.app;

import ru.otus.socket.sms.messagesystem.Addressee;

import javax.websocket.Session;

public interface MessageWebSocket extends Addressee {
    Session getSession();
}
