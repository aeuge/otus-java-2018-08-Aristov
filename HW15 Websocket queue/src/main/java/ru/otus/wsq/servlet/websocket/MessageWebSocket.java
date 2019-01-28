package ru.otus.wsq.servlet.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.wsq.messagesystem.Message;
import ru.otus.wsq.messagesystem.MessageSystem;

import java.util.Set;

@WebSocket
public class MessageWebSocket {
    private Set<MessageWebSocket> users;
    private Session session;

    @Autowired
    private MessageSystem messageSystem;

    public MessageWebSocket(Set<MessageWebSocket> users) {
        this.users = users;
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            Message message = new Message(this,data);
            messageSystem.sendMessageToDB(message);
            System.out.println("Sending message to DB: " + data);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        users.add(this);
        setSession(session);
        System.out.println("onOpen");
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        users.remove(this);
        System.out.println("onClose");
    }

    public void sendMessage(Message message){

    }
}
