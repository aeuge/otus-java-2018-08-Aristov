package ru.otus.wsq.messagesystem;

public interface MessageSystem {
     void sendMessageToDB(Message message);
     void sendMessageToFS(Message message);
}
