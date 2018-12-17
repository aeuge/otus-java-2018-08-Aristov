package ru.otus.web.servlet;

import ru.otus.web.DBservice.DBService;
import ru.otus.web.dataset.AddressDataSet;
import ru.otus.web.dataset.PhoneDataSet;
import ru.otus.web.dataset.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewUserServlet extends HttpServlet {

    private static final String NEWUSER_PAGE_TEMPLATE = "newuser.html";
    private DBService dbService;

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public NewUserServlet(DBService dbService, TemplateProcessor templateProcessor) throws IOException {
        this.dbService = dbService;
        this.templateProcessor = templateProcessor;
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();

        String getName = requestSafe(request,"name");
        String getAge = requestSafe(request,"age");
        String getAddress = requestSafe(request,"address");
        String getPhones = requestSafe(request,"phones");
        String[] phones = getPhones.split(";");
        int age = 1;
        if (!(getAge.equals(""))) {
            age = Integer.decode(getAge);
        }

        UsersDataSet uds = new UsersDataSet(getName, age , new AddressDataSet(getAddress),phones);
        long id = 0;
        String result = "";
        try {
            dbService.save(uds);
            id = uds.getId();
        } catch (Exception e) {
            result = e.getMessage();
        }
        pageVariables.put("info", result != "" ? result : "Пользователь успешно создан с ID "+Long.toString(id));
        return pageVariables;
    }

    private String requestSafe(HttpServletRequest request, String name) {
        String getName;
        try {
            getName = request.getParameter(name);
        } catch (Exception e) {
            getName = "";
        }
        return getName;
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
