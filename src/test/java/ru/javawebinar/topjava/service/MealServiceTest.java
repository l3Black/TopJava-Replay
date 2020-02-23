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

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.USER;


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
        Meal actual = service.get(MEAL1.getId(), USER.getId());
        assertThat(actual).isEqualToComparingFieldByField(MEAL1);
    }

    @Test
    public void delete() {
        service.delete(MEAL5.getId(), USER.getId());
        assertThat(service.getAll(USER.getId())).usingFieldByFieldElementComparator().isEqualTo(getAllForUserWithoutDeleted(MEAL5));
    }

    @Test
    public void getBetweenHalfOpen() {
        LocalDate start = LocalDate.of(2020, 1, 31);
        LocalDate end = LocalDate.of(2020, 2, 2);
        List<Meal> actual = service.getBetweenHalfOpen(start, end, USER.getId());
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(getBetweenHalfOpenForUser(start, end));
    }

    @Test
    public void getAll() {
        assertThat(service.getAll(USER.getId())).usingFieldByFieldElementComparator().isEqualTo(getAllForUser());
    }

    @Test
    public void update() {
        Meal updated = getUpdated(MEAL1);
        service.update(updated, USER.getId());
        assertThat(service.get(MEAL1.getId(), USER.getId())).isEqualToComparingFieldByField(updated);
    }

    @Test
    public void create() {
        Meal newMeal = MealTestData.getNew();
        Meal created = service.create(newMeal, USER.getId());
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertThat(created).isEqualToComparingFieldByField(newMeal);
        assertThat(service.get(newId, USER.getId())).isEqualToComparingFieldByField(newMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(MEAL2.getId(), ADMIN.getId());
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(MEAL5.getId(), ADMIN.getId());
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(MEAL2, ADMIN.getId());
    }
}