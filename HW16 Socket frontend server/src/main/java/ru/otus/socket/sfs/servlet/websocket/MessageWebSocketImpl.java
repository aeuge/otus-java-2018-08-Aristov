package ru.otus.socket.sfs.servlet.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.socket.sms.channel.ManagedMsgSocketWorker;
import ru.otus.socket.sms.channel.SocketMsgWorker;
import ru.otus.socket.sms.dataset.UsersDataSet;
import ru.otus.socket.sms.messagesystem.Address;
import ru.otus.socket.sms.messagesystem.Message;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ServerEndpoint("/message")
public class MessageWebSocketImpl implements MessageWebSocket {
    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    private Address address;
    private Queue<MessageWebSocketImpl> users = new ConcurrentLinkedQueue<>();
    private Session session;
    private SocketMsgWorker client;

    public MessageWebSocketImpl() throws IOException {
        address = new Address();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        client = new ManagedMsgSocketWorker(HOST, PORT);
        client.init(address);
        System.out.println("mws created");
    }

    @OnMessage
    public void onMessage(String data) {
        try {
            Message message = new Message(getAddress(),new Address("DBService"),data);
            client.send(message);
            System.out.println("Sending messageWS to DB: " + data);
            Message msg = client.take();
            session.getBasicRemote().sendText(msg.getMessage());
            System.out.println("sending message to front");
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
        System.out.println("onClose");
    }

    public Address getAddress() {
        return address;
    }

}
