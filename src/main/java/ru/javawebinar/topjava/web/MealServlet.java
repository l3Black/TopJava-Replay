package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.CrudMeals;
import ru.javawebinar.topjava.repository.InMemoryMeals;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static CrudMeals repository = new InMemoryMeals();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        action = action == null ? "" : action;
        switch (action) {
            case ("delete"):
                log.debug("delete meal id=" + req.getParameter("id"));
                repository.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect("meals");
                break;
            case ("create"):
                log.debug("create meal");
                req.getRequestDispatcher("/mealEdit.jsp").forward(req, resp);
                break;
            case ("update"):
                log.debug("update meal id=" + req.getParameter("id"));
                Meal meal = repository.get(Integer.parseInt(req.getParameter("id")));
                req.setAttribute("meal", meal);
                req.getRequestDispatcher("/mealEdit.jsp").forward(req, resp);
                break;
            default:
                log.debug("get all meals");
                req.setAttribute("meals", repository.getAll());
                req.setAttribute("formatter", formatter);
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("doPost mealServlet");

        Integer id = req.getParameter("id").isEmpty() ? null : Integer.parseInt(req.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("date"), DateTimeFormatter.ISO_DATE_TIME);
        String description = req.getParameter("description");
        int calories = req.getParameter("calories").isEmpty() ? 0 : Integer.parseInt(req.getParameter("calories"));
        log.debug("edit meal id=" + id + " dateTime=" + dateTime + " description=" + description + " calories=" + calories);

        Meal meal = new Meal(id, dateTime, description, calories);
        repository.create(meal);
        resp.sendRedirect("meals");
    }
}
