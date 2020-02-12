package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMeals implements CrudMeals {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    private final AtomicInteger sequence = new AtomicInteger(100000);

    {
        MealsUtil.meals.forEach(this::create);
    }

    @Override
    public void create(Meal meal) {
        if (meal.getId() == null) {
            int id = sequence.incrementAndGet();
            meal.setId(id);
            repository.put(id, meal);
        } else
            update(meal);
    }

    @Override
    public void delete(int id) {
        repository.remove(id);
    }

    @Override
    public void update(Meal meal) {
        repository.replace(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public Meal get(int id) {
        return repository.get(id);
    }
}
