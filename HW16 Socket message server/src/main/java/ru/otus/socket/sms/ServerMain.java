package ru.otus.socket.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import ru.otus.socket.sms.runner.ProcessRunnerImpl;
import ru.otus.socket.sms.server.SocketMsgServer;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configurable
public class ServerMain {
    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

    private static final String DBService_START_COMMAND = "java -jar ../HW16 Socket DBService/target/DBService.jar";
    private static final int CLIENT_START_DELAY_SEC = 1;
    @Autowired
    public SocketMsgServer server;

    public static void main(String[] args) throws Exception {
        new ServerMain().start();
    }

    private void start() throws Exception {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        //startClient(DBService_START_COMMAND, executorService);
        server.start();
        executorService.shutdown();
    }

    private void startClient(String command, ScheduledExecutorService executorService) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(command);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, CLIENT_START_DELAY_SEC , TimeUnit.SECONDS);
    }

}
