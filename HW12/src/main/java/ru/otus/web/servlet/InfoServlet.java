package ru.otus.web.servlet;

import ru.otus.web.DBservice.DBService;
import ru.otus.web.dataset.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class InfoServlet extends HttpServlet {

    private static final String INFO_PAGE_TEMPLATE = "info.html";
    private DBService dbService;
    String readUserDataSetForID = "";

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public InfoServlet(DBService dbService, TemplateProcessor templateProcessor) throws IOException {
        this.dbService = dbService;
        this.templateProcessor = templateProcessor;
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();

        readUserDataSetForID = "";
        String idString = request.getParameter("id");
        long id = Long.parseLong(idString);
        UsersDataSet uds = null;
        try {
            uds = this.dbService.read(id, UsersDataSet.class);
            if (uds != null) {
                readUserDataSetForID = uds.toHTMLString();
            }
        } catch (SQLException e) {
            readUserDataSetForID = "";
        }
        pageVariables.put("info", readUserDataSetForID != "" ? readUserDataSetForID : "Пользователь с данным id не найден");
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
