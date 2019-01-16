package ru.otus.war.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.war.DBservice.DBService;
import ru.otus.war.dataset.UsersDataSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoServlet extends HttpServlet {

    private static final String INFO_PAGE_TEMPLATE = "info.html";
    private static final String VARIABLE_USERS = "Users";
    private static final String VARIABLE_ID = "id";
    private DBService dbService;

    private TemplateProcessor templateProcessor;

    public void init() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext(
                        "SpringBeans.xml");
        dbService = context.getBean("dbService", DBService.class);
        templateProcessor = context.getBean("templateProcessor", TemplateProcessor.class);
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();

        String idString = request.getParameterMap().containsKey(VARIABLE_ID)?request.getParameter(VARIABLE_ID):"0";
        long id = Long.parseLong(idString);
        List<UsersDataSet> listOfUsers = new ArrayList<>();
        UsersDataSet uds;
        try {
            uds = this.dbService.read(id, UsersDataSet.class);
            if (uds != null) {
                listOfUsers.add(uds);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pageVariables.put(VARIABLE_USERS, listOfUsers);
        return pageVariables;
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Object isAdmin = request.getSession().getAttribute("admin");
        if (isAdmin != null) {
            if (isAdmin.equals("true")) {
                Map<String, Object> pageVariables = createPageVariablesMap(request);

                response.setContentType("text/html;charset=utf-8");
                String page = templateProcessor.getPage(INFO_PAGE_TEMPLATE, pageVariables);
                response.getWriter().println(page);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}