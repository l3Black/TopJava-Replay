package ru.javawebinar.topjava.service.user;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(ADMIN_ID);
        MEAL_MATCHER.assertMatch(user.getMeals(), ADMIN_MEAL1, ADMIN_MEAL2);
    }
}
