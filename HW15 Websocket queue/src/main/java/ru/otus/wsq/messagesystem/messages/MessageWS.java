package ru.otus.wsq.messagesystem.messages;

import lombok.Data;
import ru.otus.wsq.messagesystem.Address;
import ru.otus.wsq.messagesystem.MessageToFrontend;
import ru.otus.wsq.servlet.websocket.MessageWebSocket;
import java.io.IOException;

@Data
public class MessageWS extends MessageToFrontend {
    private String message;

    public MessageWS(Address from, Address to, String message) {
        super(from, to);
        this.message = message;
    }

    @Override
    public void exec(MessageWebSocket mws) {
        try {
            mws.getSession().getRemote().sendString(message);
            System.out.println("sending message to frontend");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
