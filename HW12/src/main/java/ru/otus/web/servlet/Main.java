package ru.otus.web.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.web.DBservice.DBService;
import ru.otus.web.DBservice.DBServiceHibernateImpl;
import ru.otus.web.dataset.AddressDataSet;
import ru.otus.web.dataset.PhoneDataSet;
import ru.otus.web.dataset.UsersDataSet;


public class Main {
    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "public_html";


    public static void main(String[] args) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);
        resourceHandler.setWelcomeFiles(new String[]{"index.htm"});

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();

        DBService dbService = new DBServiceHibernateImpl();
        loadDefaultDat(dbService);

        context.addServlet(IndexServlet.class, "/index.html");
        context.addServlet(new ServletHolder(new AdminServlet(dbService, templateProcessor)), "/admin");
        context.addServlet(new ServletHolder(new InfoServlet(dbService, templateProcessor)), "/info");
        context.addServlet(new ServletHolder(new NewUserServlet(dbService, templateProcessor)), "/newuser");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }

    private static void loadDefaultDat(DBService dbService) {
        try {
            UsersDataSet user1 = new UsersDataSet(
                    "tully",
                    new AddressDataSet("Mira"),
                    new PhoneDataSet("+1 234 567 8018"),
                    new PhoneDataSet("+7 987 645 4545")
            );
            dbService.save(user1);
            UsersDataSet user2 = new UsersDataSet(
                    "sully",
                    new AddressDataSet("Lenina"),
                    new PhoneDataSet("+951 295 50 50"),
                    new PhoneDataSet("+7 987 111 0000")
            );
            dbService.save(user2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
