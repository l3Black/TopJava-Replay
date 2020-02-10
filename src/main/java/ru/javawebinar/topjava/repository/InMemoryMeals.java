package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMeals implements CrudMeals {
    private static final Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    private static AtomicInteger sequence = new AtomicInteger(100000);

    private static InMemoryMeals instance;

    {
        MealsUtil.meals.forEach(this::create);
    }

    //Singleton because when you create a new instance, the repository will go bad
    private InMemoryMeals() {
    }

    public static synchronized InMemoryMeals getInstance() {
        if (instance == null)
            instance = new InMemoryMeals();
        return instance;
    }

    @Override
    public void create(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(sequence.incrementAndGet());
            repository.put(sequence.get(), meal);
        } else
            update(meal);
    }

    @Override
    public void delete(int id) {
        repository.remove(id);
    }

    @Override
    public void update(Meal meal) {
        if (repository.containsKey(meal.getId()))
            repository.put(meal.getId(), meal);
        else
            throw new IllegalArgumentException("Meal with non-existent id");
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
