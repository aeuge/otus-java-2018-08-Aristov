package ru.otus.socket.sfs.servlet.websocket;

import ru.otus.socket.sfs.messagesystem.Addressee;

import javax.websocket.Session;

public interface MessageWebSocket {
    Session getSession();
}
