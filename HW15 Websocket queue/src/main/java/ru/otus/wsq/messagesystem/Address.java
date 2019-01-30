package ru.otus.wsq.messagesystem;

import lombok.Data;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public final class Address {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final String id;

    public Address(){
        id = String.valueOf(ID_GENERATOR.getAndIncrement());
    }

    public Address(String id) {
        this.id = id;
    }
}
