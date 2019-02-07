package ru.otus.socket.sms;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.socket.sms.runner.ProcessRunnerImpl;
import ru.otus.socket.sms.server.SocketMsgServer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private static final String WAR_FILE = "/HW16 Socket frontend server/target/HW16-sfs.war";
    private static final String WAR_DESTINATION = "/webapps/";
    private static final String BAT_RUNNER = "cmd.exe /K ";

    private static final String TOMCAT_SERVER = "/bin/startup.bat";
    private static String CATALINE_HOME = "c:/apps/apache-tomcat-8.5.37";
    private static String APP_HOME = "";
    private static final int CLIENT_START_DELAY_SEC = 2;
    public SocketMsgServer server;

    public static void main(String[] args) throws Exception {
        if (args.length>0) CATALINE_HOME = args[0];
        new ServerMain().start();
    }

    private void start() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("springBeans.xml");
        server = (SocketMsgServer) context.getBean("socketMsgServer");
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String[] ss = s.replace("\\","/").split("/");
        s = ss[0];
        for (int i = 1; i < ss.length-1 ; i++) {
            s = s + "/" + ss[i];
        }
        APP_HOME = s;
        System.out.println("Current relative path is: " + s);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Files.copy(Paths.get(APP_HOME + WAR_FILE), Paths.get(CATALINE_HOME + WAR_DESTINATION + "ROOT.war"), StandardCopyOption.REPLACE_EXISTING);
        startClient(BAT_RUNNER + CATALINE_HOME + TOMCAT_SERVER, executorService);
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
