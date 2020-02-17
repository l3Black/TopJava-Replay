package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            log.info("save {}", meal);
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        log.info("update {}", meal);
        Meal oldMeal = repository.get(meal.getId());
        if (oldMeal != null && oldMeal.getUserId() == userId) {
            meal.setUserId(userId);
            return repository.put(meal.getId(), meal);
        } else
            return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete id={}", id);
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() == userId) {
            repository.remove(id);
            return true;
        } else
            return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get id={}", id);
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() == userId)
            return meal;
        else
            return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return repository.values().stream()
                .filter(m -> m.getUserId() == userId)
                .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate()))
                .collect(Collectors.toList());
    }
}

