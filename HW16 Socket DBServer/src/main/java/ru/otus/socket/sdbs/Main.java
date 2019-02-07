package ru.otus.socket.sdbs;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.socket.sms.dataset.UsersDataSet;
import ru.otus.socket.sdbs.DBservice.DBService;
import ru.otus.socket.sms.channel.ManagedMsgSocketWorker;
import ru.otus.socket.sms.channel.SocketMsgWorker;
import ru.otus.socket.sms.messagesystem.Address;
import ru.otus.socket.sms.messagesystem.Message;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private Address address;

    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    private DBService dbService;

    public static void main(String[] args) throws Exception {
        new Main().start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("springBeansDB.xml");
        dbService = (DBService) context.getBean("dbService");
        address = dbService.getAddress();


        SocketMsgWorker client = new ManagedMsgSocketWorker(HOST, PORT);
        client.init(address);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (true) {
                    Message msg = client.take();
                    Message messageToFrontend = new Message(msg.getTo(), msg.getFrom(),"");
                    UsersDataSet uds = dbService.read(Integer.decode(msg.getMessage()),UsersDataSet.class);
                    if (uds==null) {
                        messageToFrontend.setMessage("Объекта с таким ID не существует");
                    } else {
                        Gson gson = new GsonBuilder().setExclusionStrategies(new PhoneUserDataSetExcluder()).create();
                        String json = gson.toJson(uds);
                        messageToFrontend.setMessage(json);
                    }
                    client.send(messageToFrontend);
                    System.out.println("put message to WS");
                }
            } catch (InterruptedException | SQLException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
    }
    class PhoneUserDataSetExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(UsersDataSet.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }
}
