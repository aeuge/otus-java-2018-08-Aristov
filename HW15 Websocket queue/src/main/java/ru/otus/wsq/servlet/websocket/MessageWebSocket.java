package ru.otus.wsq.servlet.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.wsq.messagesystem.*;
import ru.otus.wsq.messagesystem.messages.MessageDB;
import java.util.Set;

@WebSocket
public class MessageWebSocket implements Addressee {
    private Address address;
    private Set<MessageWebSocket> users;
    private Session session;

    @Autowired
    private MessageSystemContext context;

    public MessageWebSocket(Set<MessageWebSocket> users) {
        this.users = users;
        address = new Address();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        System.out.println("mws created");
        context.getMessageSystem().addAddressee(this);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            MessageDB messageDB = new MessageDB(getAddress(),context.getDbAddress(),data);
            context.getMessageSystem().sendMessage(messageDB);
            System.out.println("Sending messageWS to DB: " + data);
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
        context.getMessageSystem().endThread(this);
        System.out.println("onClose");
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }
}
