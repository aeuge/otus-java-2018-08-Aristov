package ru.otus.wsq.app;

import ru.otus.wsq.messagesystem.Address;
import ru.otus.wsq.messagesystem.Addressee;
import ru.otus.wsq.messagesystem.Message;

public abstract class MessageToFrontend extends Message {
    public MessageToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof MessageWebSocket) {
            exec((MessageWebSocket) addressee);
        }
    }

    public abstract void exec(MessageWebSocket messageWebSocket);
}