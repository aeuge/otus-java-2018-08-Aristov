package ru.otus.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import ru.otus.web.servlet.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private final static int PORT = 8090;

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);
        server.setHandler(new HandlerList(ServletInitializer.initResourceHandler(), ServletInitializer.initContext()));
        server.start();
        server.join();
    }
}
