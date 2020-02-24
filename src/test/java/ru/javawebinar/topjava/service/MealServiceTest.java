package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.getUpdated;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal actual = service.get(MEAL1.getId(), USER_ID);
        assertMatch(actual, MEAL1);
    }

    @Test
    public void delete() {
        service.delete(MEAL5.getId(), USER_ID);
        List<Meal> actual = service.getAll(USER_ID);
        List<Meal> expected = getAllForUserWithoutDeleted(MEAL5);
        assertMatch(actual, expected);
    }

    @Test
    public void getBetweenHalfOpen() {
        LocalDate start = LocalDate.of(2020, 1, 31);
        LocalDate end = LocalDate.of(2020, 2, 2);
        List<Meal> actual = service.getBetweenHalfOpen(start, end, USER_ID);
        List<Meal> expected = getBetweenHalfOpenForUser(start, end);
        assertMatch(actual, expected);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        List<Meal> expected = getAllForUser();
        assertMatch(actual, expected);
    }

    @Test
    public void update() {
        Meal updated = getUpdated(MEAL1);
        service.update(updated, USER.getId());
        Meal actual = service.get(MEAL1.getId(), USER_ID);
        assertMatch(actual, updated);
    }

    @Test
    public void create() {
        Meal newMeal = MealTestData.getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        Meal actual = service.get(newId, USER_ID);
        assertMatch(created, newMeal);
        assertMatch(actual, newMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(MEAL2.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(MEAL5.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(MEAL2, ADMIN_ID);
    }
}