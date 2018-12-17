package ru.otus.web;

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
import ru.otus.web.db.DBDataInitializer;
import ru.otus.web.servlet.*;


public class Main {
    private final static int PORT = 8090;


    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);
        server.setHandler(new HandlerList(ServletInitializer.initResourceHandler(), ServletInitializer.initContext()));

        server.start();
        server.join();
    }
}
