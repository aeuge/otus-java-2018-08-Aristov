package ru.otus.wsq.app;

import ru.otus.wsq.messagesystem.Addressee;

import javax.websocket.Session;

public interface MessageWebSocket extends Addressee {
    Session getSession();
}
