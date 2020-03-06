package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    int deleteByIdAndUserId(Integer id, Integer userId);

    Meal findByIdAndUserId(Integer integer, Integer userId);

    List<Meal> findAllByUserIdOrderByDateTimeDesc(Integer userId);

    List<Meal> findAllByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTimeDesc(Integer userId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:userId")
    Meal getWithUser(@Param("id") Integer id, @Param("userId") Integer userId);
}
