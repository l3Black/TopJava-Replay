package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
@RequestMapping("/meals")
public class JspMealController {

    private static final Logger log = getLogger(JspMealController.class);

    private MealService service;

    public JspMealController(MealService service) {
        this.service = service;
    }

    @GetMapping("")
    public String getAll(Model model) {
        log.info("getAll for user {}", authUserId());
        List<Meal> meals = service.getAll(authUserId());
        List<MealTo> mealTos = MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
        model.addAttribute("meals", mealTos);
        return "meals";
    }

    @GetMapping("/filter")
    public String getAllBetweenInclusive(Model model, @Param("startDate") @Nullable String startDate, @Param("endDate") @Nullable String endDate,
                                         @Param("startTime") @Nullable String startTime, @Param("endTime") @Nullable String endTime) {
        LocalDate sd = parseLocalDate(startDate);
        LocalDate ed = parseLocalDate(endDate);
        LocalTime st = parseLocalTime(startTime);
        LocalTime et = parseLocalTime(endTime);
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", sd, ed, st, et, authUserId());
        List<Meal> meals = service.getBetweenInclusive(sd, ed, authUserId());
        List<MealTo> mealTos = MealsUtil.getFilteredTos(meals, SecurityUtil.authUserCaloriesPerDay(), st, et);
        model.addAttribute("meals", mealTos);
        return "meals";
    }


    @GetMapping("/delete")
    public String delete(@Param("id") Integer id) {
        log.info("delete meal {} for user {}", id, authUserId());
        service.delete(id, authUserId());
        return "redirect:/meals";
    }

    @GetMapping("/update")
    public String createOrUpdate(Model model, @Param("id") @Nullable Integer id) {
        Meal meal = new Meal(id, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute(meal);
        return "mealForm";
    }

    @PostMapping("")
    public String update(HttpServletRequest req) {
        Meal meal = new Meal(
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));

        if (StringUtils.isEmpty(req.getParameter("id"))) {
            service.create(meal, authUserId());
        } else {
            meal.setId(Integer.parseInt(req.getParameter("id")));
            service.update(meal, authUserId());
        }
        return "redirect:/meals";
    }
}
