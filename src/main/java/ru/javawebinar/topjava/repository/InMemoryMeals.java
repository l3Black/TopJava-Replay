package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMeals implements CrudMeals {
    private static final ConcurrentHashMap<Integer, Meal> repository = new ConcurrentHashMap<>();

    private static AtomicInteger sequence = new AtomicInteger(100000);

    static {
        MealsUtil.meals.forEach(meal -> {
            meal.setId(sequence.incrementAndGet());
            repository.put(meal.getId(), meal);
        });
    }

    @Override
    public void create(Meal meal) {
        if (meal.getId() != null)
            update(meal);
        else {
            meal.setId(sequence.incrementAndGet());
            repository.put(sequence.get(), meal);
        }
    }

    @Override
    public void delete(int id) {
        repository.remove(id);
    }

    @Override
    public void update(Meal meal) {
        repository.put(meal.getId(), meal);
    }

    @Override
    public List<MealTo> getAll() {
        return MealsUtil.filteredByStreams(new ArrayList<>(repository.values()), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
    }

    @Override
    public Meal get(int id) {
        return repository.get(id);
    }
}
