package ru.otus.wsq.app;

import lombok.Data;
import ru.otus.wsq.messagesystem.Address;
import ru.otus.wsq.messagesystem.MessageSystem;

@Data
public class MessageSystemContext {
    private final MessageSystem messageSystem;

    private Address frontAddress;
    private Address dbAddress;

    public MessageSystemContext(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }
}
