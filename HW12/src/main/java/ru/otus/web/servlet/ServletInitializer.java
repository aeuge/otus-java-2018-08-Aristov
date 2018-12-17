package ru.otus.web.servlet;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.web.DBservice.DBService;
import ru.otus.web.DBservice.DBServiceHibernateImpl;
import ru.otus.web.db.DBDataInitializer;

public class ServletInitializer {
    private final static String PUBLIC_HTML = "public_html";
    private final static String INDEX_HTML = "/index.htm";

    public static ResourceHandler initResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);
        resourceHandler.setWelcomeFiles(new String[]{INDEX_HTML});
        return resourceHandler;
    }
    public static ServletContextHandler initContext() throws Exception {
        DBService dbService = new DBServiceHibernateImpl();
        DBDataInitializer.initDbWithTestData(dbService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();
        context.addServlet(IndexServlet.class, "/index.html");
        context.addServlet(new ServletHolder(new AdminServlet(dbService, templateProcessor)), "/admin");
        context.addServlet(new ServletHolder(new InfoServlet(dbService, templateProcessor)), "/info");
        context.addServlet(new ServletHolder(new NewUserServlet(dbService, templateProcessor)), "/newuser");
        return context;
    }
}
