package com.myershome.homeapp.repository;

import com.myershome.homeapp.model.Meal;
import com.myershome.homeapp.services.Constants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {
    Optional<Meal> findByMealDay(Constants.Days day);

    List<Meal> findAllByMealDayIsNull();

    List<Meal> findAllByMealDayNotNull();
}
