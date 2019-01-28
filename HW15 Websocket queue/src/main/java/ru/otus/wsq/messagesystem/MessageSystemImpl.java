package ru.otus.wsq.messagesystem;

import ru.otus.wsq.DBservice.DBService;
import ru.otus.wsq.dataset.UsersDataSet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MessageSystemImpl implements MessageSystem{
    private final static Logger logger = Logger.getLogger(MessageSystemImpl.class.getName());
    private static final int DEFAULT_STEP_TIME = 10;

    private final List<Thread> workers;
    private final LinkedBlockingQueue<Message> messagestoDB;
    private final LinkedBlockingQueue<Message> messagestoFS;

    private DBService dbService;

    public MessageSystemImpl(DBService dbService) {
        this.dbService = dbService;
        workers = new ArrayList<>();
        messagestoDB = new LinkedBlockingQueue<>();
        messagestoFS = new LinkedBlockingQueue<>();
        startDB();
        startFS();
        System.out.println("ms created");
    }

    public void sendMessageToDB(Message message) { messagestoDB.add(message); }

    public void sendMessageToFS(Message message) { messagestoFS.add(message); }

    public void startDB() {
            String name = "MS-worker-DB";
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        Message message = messagestoDB.take();
                        UsersDataSet uds = dbService.read(Integer.decode(message.getMessage()),UsersDataSet.class);
                        if (uds==null) {
                            message.setMessage("Объекта с таким ID не существует");
                        } else
                            message.setMessage(uds.toString());
                        messagestoFS.add(message);
                        logger.log(Level.INFO,"Sending message to FS: " + message.getMessage());
                    } catch (InterruptedException e) {
                        logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                        return;
                    } catch (SQLException e) {
                        logger.log(Level.INFO, "SQL Exception: " + name);
                        return;
                    }
                }
            });
            thread.setName(name);
            thread.start();
            workers.add(thread);
    }

    public void startFS() {
        String name = "MS-worker-FS";
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Message message = messagestoFS.take();
                    message.getMws().getSession().getRemote().sendString(message.getMessage());
                } catch (InterruptedException e) {
                    logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                    return;
                } catch (IOException e) {
                    logger.log(Level.INFO, "IO Exception: " + name);
                    return;
                }
            }
        });
        thread.setName(name);
        thread.start();
        workers.add(thread);
    }

    public void dispose() {
        workers.forEach(Thread::interrupt);
    }
}
