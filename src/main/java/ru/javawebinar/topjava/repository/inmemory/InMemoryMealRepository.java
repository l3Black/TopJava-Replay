package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
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
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            repository.putIfAbsent(userId, new HashMap<>());
            log.info("save {}", meal);
            meal.setId(counter.incrementAndGet());
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        log.info("update {}", meal);
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
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
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) return Collections.emptyList();
        return mealMap.values().stream()
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetweenInclusive(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getBetween start={} end={}", startDate, endDate);
        return getAll(userId)
                .stream()
                .filter(meal -> DateTimeUtil.isBetweenInclusive(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

