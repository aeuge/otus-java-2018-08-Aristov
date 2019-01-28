package ru.otus.wsq.servlet.websocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author v.chibrikov
 */
public class MessageWebSocketCreator implements WebSocketCreator {
    private Set<MessageWebSocket> users;

    public MessageWebSocketCreator() {
        this.users = Collections.newSetFromMap(new ConcurrentHashMap<>());
        System.out.println("WebSocketCreator created");
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        MessageWebSocket socket = new MessageWebSocket(users);
        System.out.println("Socket created");
        return socket;
    }
}
