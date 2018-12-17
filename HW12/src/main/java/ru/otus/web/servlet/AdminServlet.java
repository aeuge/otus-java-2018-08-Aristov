package ru.otus.web.servlet;

import ru.otus.web.DBservice.DBService;
import ru.otus.web.DBservice.DBServiceHibernateImpl;
import ru.otus.web.dataset.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminServlet extends HttpServlet {

    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";
    private static final String ADMIN_NAME = "admin";
    private static final String VARIABLE_USERS = "Users";
    private static final String VARIABLE_NUMBER_OF_USERS = "numberOfUsers";
    private DBService dbService;
    private int numberOfUsers = 0;

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public AdminServlet(DBService dbService, TemplateProcessor templateProcessor) throws IOException {
        this.dbService = dbService;
        this.templateProcessor = templateProcessor;
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        List<UsersDataSet> uds = this.dbService.readAll(UsersDataSet.class);
        numberOfUsers = uds.size();
        pageVariables.put(VARIABLE_USERS, uds);
        pageVariables.put(VARIABLE_NUMBER_OF_USERS, numberOfUsers);
        return pageVariables;
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Object isAdmin = request.getSession().getAttribute(ADMIN_NAME);
        if (isAdmin != null) {
            if (isAdmin.equals("true")) {
                Map<String, Object> pageVariables = createPageVariablesMap(request);

                response.setContentType("text/html;charset=utf-8");
                String page = templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables);
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
