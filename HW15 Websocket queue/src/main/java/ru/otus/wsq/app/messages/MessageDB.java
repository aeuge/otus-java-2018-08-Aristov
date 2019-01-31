package ru.otus.wsq.app.messages;

import lombok.Data;
import ru.otus.wsq.app.DBService;
import ru.otus.wsq.dataset.UsersDataSet;
import ru.otus.wsq.messagesystem.Address;
import ru.otus.wsq.app.MessageToDB;

import java.sql.SQLException;

@Data
public class MessageDB extends MessageToDB {
    private String message;

    public MessageDB(Address from, Address to, String message) {
        super(from, to);
        this.message = message;
    }

    @Override
    public void exec(DBService dbService) {
        try {
            MessageWS messageToFrontend = new MessageWS(getTo(), getFrom(),"");
            UsersDataSet uds = dbService.read(Integer.decode(message),UsersDataSet.class);
            if (uds==null) {
                messageToFrontend.setMessage("Объекта с таким ID не существует");
            } else
                messageToFrontend.setMessage(uds.toString());
            dbService.getMS().sendMessage(messageToFrontend);
            System.out.println("put message to WS");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
