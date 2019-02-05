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
import java.util.Map;

@Configurable
public class NewUserServlet extends HttpServlet {
    private static final String NEWUSER_PAGE_TEMPLATE = "newuser.html";
    private static final String ADMIN_NAME = "admin";
    private static final String VARIABLE_NAME = "name";
    private static final String VARIABLE_AGE = "age";
    private static final String VARIABLE_ADDRESS = "address";
    private static final String VARIABLE_PHONES = "phones";
    @Autowired
    private TemplateProcessor templateProcessor;

    @Override
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();

        String getName = request.getParameterMap().containsKey(VARIABLE_NAME)?request.getParameter(VARIABLE_NAME):"";
        String getAge = request.getParameterMap().containsKey(VARIABLE_AGE)?request.getParameter(VARIABLE_AGE):"";
        String getAddress = request.getParameterMap().containsKey(VARIABLE_ADDRESS)?request.getParameter(VARIABLE_ADDRESS):"";
        String getPhones = request.getParameterMap().containsKey(VARIABLE_PHONES)?request.getParameter(VARIABLE_PHONES):"";

        String[] phones = getPhones.split(";");
        int age = 1;
        if (!(getAge.equals(""))) {
            age = Integer.decode(getAge);
        }
        String result = "";
        /*UsersDataSet uds = new UsersDataSet(getName, age , new AddressDataSet(getAddress),phones);
        long id = 0;

        try {
            dbService.save(uds);
            id = uds.getId();
        } catch (Exception e) {
            result = e.getMessage();
        }*/
        pageVariables.put("info", result != "" ? result : "Пользователь успешно создан с ID "+1);//Long.toString(id));
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
                String page = templateProcessor.getPage(NEWUSER_PAGE_TEMPLATE, pageVariables);
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
