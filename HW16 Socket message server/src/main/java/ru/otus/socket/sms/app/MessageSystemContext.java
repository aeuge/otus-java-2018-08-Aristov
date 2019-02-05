package ru.otus.socket.sms.app;

import lombok.Data;
import ru.otus.socket.sms.messagesystem.Address;
import ru.otus.socket.sms.messagesystem.MessageSystem;

@Data
public class MessageSystemContext {
    private final MessageSystem messageSystem;

    private Address frontAddress;
    private Address dbAddress;

    public MessageSystemContext(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }
}
