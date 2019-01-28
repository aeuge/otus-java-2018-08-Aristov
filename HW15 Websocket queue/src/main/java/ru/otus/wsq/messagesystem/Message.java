package ru.otus.wsq.messagesystem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.wsq.servlet.websocket.MessageWebSocket;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private MessageWebSocket mws;
    private String message;
}
