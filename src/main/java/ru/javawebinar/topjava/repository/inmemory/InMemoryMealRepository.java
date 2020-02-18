package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.UsersUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        UsersUtil.USERS.forEach(user -> repository.put(user.getId(), new ConcurrentHashMap<>()));
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) return null;
        if (meal.isNew()) {
            log.info("save {}", meal);
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        log.info("update {}", meal);
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete id={}", id);
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get id={}", id);
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) return null;
        return mealMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        repository.putIfAbsent(userId, new ConcurrentHashMap<>());
        return repository.get(userId).values().stream()
                .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetweenInclusive(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getBetween start={} end={}", startDate, endDate);
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) return null;
        return mealMap.values()
                .stream()
                .filter(meal -> DateTimeUtil.isBetweenInclusive(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

