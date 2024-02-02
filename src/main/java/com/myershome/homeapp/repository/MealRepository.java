package com.myershome.homeapp.repository;

import com.myershome.homeapp.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
