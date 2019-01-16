package ru.otus.war.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IndexServlet extends HttpServlet {

    private static final String DEFAULT_USER_NAME = "UNKNOWN";
    private static final String INDEX_PAGE_TEMPLATE = "index.html";
    private static final String ADMIN_PAGE_PASSWORD = "password";

    private TemplateProcessor templateProcessor;

    public void init() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext(
                        "SpringBeans.xml");
        templateProcessor = context.getBean("templateProcessor", TemplateProcessor.class);
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        String requestPassword = request.getParameter(ADMIN_PAGE_PASSWORD);
        if (requestPassword==null) {requestPassword = "";}
        String loginAlert = "";
        if ((!requestPassword.equals("admin")) && (requestPassword.length()>0)) {
            loginAlert = "1";
        } else if (requestPassword.equals("admin")){
            request.getSession().setAttribute("admin", "true");
            loginAlert = "0";
        }
        pageVariables.put("loginAlert",loginAlert);
        return pageVariables;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(request);

        response.setContentType("text/html;charset=utf-8");
        String page = templateProcessor.getPage(INDEX_PAGE_TEMPLATE, pageVariables);
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
