package ru.javawebinar.topjava.service.meal;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDataJpaTest extends AbstractMealServiceTest {

    @Test
    public void getWithMeals() {
        Meal meal = service.getWithUser(MEAL1_ID, USER_ID);
        USER_MATCHER.assertMatch(meal.getUser(), USER);
    }
}
