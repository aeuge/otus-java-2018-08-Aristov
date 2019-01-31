package ru.otus.wsq.app;

import org.eclipse.jetty.websocket.api.Session;
import ru.otus.wsq.messagesystem.Addressee;

public interface MessageWebSocket extends Addressee {
    Session getSession();
}
