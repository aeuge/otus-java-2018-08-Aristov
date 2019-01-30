package ru.otus.wsq.messagesystem;

import lombok.Data;

@Data
public abstract class Message {
    private final Address from;
    private final Address to;

    public abstract void exec(Addressee addressee);
}
