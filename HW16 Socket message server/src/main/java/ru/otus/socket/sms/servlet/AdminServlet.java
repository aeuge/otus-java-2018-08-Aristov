package ru.otus.socket.sms.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configurable
public class AdminServlet extends HttpServlet {

    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";
    private static final String ADMIN_NAME = "admin";
    private static final String VARIABLE_USERS = "Users";
    private static final String VARIABLE_NUMBER_OF_USERS = "numberOfUsers";
    private static final String VARIABLE_NUMBER_OF_CACHED_USERS = "numberOfCachedUsers";
    private int numberOfUsers = 0;
    public String temp = new String();
    @Autowired
    private TemplateProcessor templateProcessor;

    @Override
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        //List<UsersDataSet> uds = this.dbService.readAll(UsersDataSet.class);
        numberOfUsers = 1;//uds.size();
        pageVariables.put(VARIABLE_USERS, null);
        pageVariables.put(VARIABLE_NUMBER_OF_USERS, numberOfUsers);
        pageVariables.put(VARIABLE_NUMBER_OF_CACHED_USERS, 1);//dbService.getCacheCount());
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
