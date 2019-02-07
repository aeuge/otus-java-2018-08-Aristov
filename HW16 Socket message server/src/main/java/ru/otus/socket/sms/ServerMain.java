package ru.otus.socket.sms;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.socket.sms.runner.ProcessRunnerImpl;
import ru.otus.socket.sms.server.SocketMsgServer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {
    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

    private static final String DBSERVER_START_COMMAND = "java -jar ../\"HW16 Socket DBServer\"/target/sdbs-jar-with-dependencies.jar";
    private static final String WAR_FILE = "c:/Otus/DZ/HW16 Socket frontend server/target/HW16-sfs.war";
    private static final String WAR_DESTINATION = "c:/apps/apache-tomcat-8.5.37/webapps/";
    private static final String TOMCAT_SERVER = "cmd.exe /C c:/apps/apache-tomcat-8.5.37/bin/startup.bat";

    private static final int CLIENT_START_DELAY_SEC = 2;
    public SocketMsgServer server;

    public static void main(String[] args) throws Exception {
        new ServerMain().start();
    }

    private void start() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("springBeans.xml");
        server = (SocketMsgServer) context.getBean("socketMsgServer");

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Files.copy(Paths.get(WAR_FILE), Paths.get(WAR_DESTINATION + "ROOT.war"), StandardCopyOption.REPLACE_EXISTING);
        startClient(TOMCAT_SERVER, executorService);
        startClient(DBSERVER_START_COMMAND, executorService);
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
