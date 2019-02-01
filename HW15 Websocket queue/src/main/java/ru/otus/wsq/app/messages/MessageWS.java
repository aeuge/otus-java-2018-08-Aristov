package ru.otus.wsq.app.messages;

import lombok.Data;
import ru.otus.wsq.app.MessageWebSocket;
import ru.otus.wsq.messagesystem.Address;
import ru.otus.wsq.app.MessageToFrontend;
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
            mws.getSession().getBasicRemote().sendText(message);
            System.out.println("sending message to frontend");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
