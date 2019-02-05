package ru.otus.socket.sms.messagesystem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Address from;
    private Address to;
    private String message;
}
