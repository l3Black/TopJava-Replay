package ru.javawebinar.topjava.service.user;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_WITHOUT_MEALS;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(ADMIN_ID);
        MEAL_MATCHER.assertMatch(user.getMeals(), ADMIN_MEAL1, ADMIN_MEAL2);
    }

    @Test
    public void getWithMealsNotFound() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.getWithMeals(10));
    }

    @Test
    public void getWithEmptyMeals() {
        USER_WITHOUT_MEALS.setMeals(Collections.emptyList());
        //noinspection ConstantConditions
        User user = service.getWithMeals(USER_WITHOUT_MEALS.getId());
        MEAL_MATCHER.assertMatch(user.getMeals(), USER_WITHOUT_MEALS.getMeals());
    }
}
