package ru.otus.wsq.messagesystem;

import ru.otus.wsq.servlet.websocket.MessageWebSocket;

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