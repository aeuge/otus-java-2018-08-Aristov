package ru.otus.socket.sdbs;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.socket.sdbs.dataset.UsersDataSet;
import ru.otus.socket.sdbs.DBservice.DBService;
import ru.otus.socket.sms.channel.SocketMsgWorker;
import ru.otus.socket.sms.messagesystem.Message;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    @Autowired
    private DBService dbService;

    public static void main(String[] args) throws Exception {
        new Main().start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() throws Exception {

        SocketMsgWorker client = new ManagedMsgSocketWorker(HOST, PORT);
        client.init();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (true) {
                    Message msg = client.take();
                    Message messageToFrontend = new Message(msg.getTo(), msg.getFrom(),"");
                    UsersDataSet uds = dbService.read(Integer.decode(msg.getMessage()),UsersDataSet.class);
                    if (uds==null) {
                        messageToFrontend.setMessage("Объекта с таким ID не существует");
                    } else
                        messageToFrontend.setMessage(uds.toString());
                    client.send(messageToFrontend);
                    System.out.println("put message to WS");
                }
            } catch (InterruptedException | SQLException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
    }

}
