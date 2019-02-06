package ru.otus.socket.sfs.servlet.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.socket.sfs.messagesystem.Address;
import ru.otus.socket.sfs.messagesystem.Message;
import ru.otus.socket.sfs.messagesystem.MessageSystem;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
//mws.getSession().getBasicRemote().sendText(message);
@ServerEndpoint("/message")
public class MessageWebSocketImpl implements MessageWebSocket {
    private Address address;
    private Queue<MessageWebSocketImpl> users = new ConcurrentLinkedQueue<>();
    private Session session;

    @Autowired
    private MessageSystemContext context;

    public MessageWebSocketImpl() {
        address = new Address();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        System.out.println("mws created");
        context.getMessageSystem().addAddressee(this);
    }

    @OnMessage
    public void onMessage(String data) {
        try {
            Message message = new Message(getAddress(),context.getDbAddress(),data);
            context.getMessageSystem().sendMessage(message);
            System.out.println("Sending messageWS to DB: " + data);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    @OnOpen
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

    @OnClose
    public void onClose() {
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
