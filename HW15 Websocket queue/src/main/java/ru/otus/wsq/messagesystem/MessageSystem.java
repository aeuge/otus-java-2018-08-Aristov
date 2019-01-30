package ru.otus.wsq.messagesystem;

public interface MessageSystem {
    void addAddressee(Addressee addressee);
    void sendMessage(Message message);
    void endThread(Addressee addressee);
    void dispose();
}
