package ru.otus.wsq.app;

import ru.otus.wsq.app.DBService;
import ru.otus.wsq.messagesystem.Address;
import ru.otus.wsq.messagesystem.Addressee;
import ru.otus.wsq.messagesystem.Message;

public abstract class MessageToDB extends Message {
    public MessageToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) {
            exec((DBService) addressee);
        }
    }

    public abstract void exec(DBService dbService);
}
