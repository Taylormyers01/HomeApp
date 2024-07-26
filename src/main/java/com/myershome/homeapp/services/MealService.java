package com.myershome.homeapp.services;

import com.myershome.homeapp.model.Meal;
import com.myershome.homeapp.repository.IngredientRepository;
import com.myershome.homeapp.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MealService {

    @Autowired
    MealRepository mealRepository;
    @Autowired
    IngredientRepository ingredientRepository;

    public Meal save(Meal meal) throws Exception {
        if(meal.getMealDay() != null && mealRepository.findByMealDay(meal.getMealDay()).isPresent() ){
            throw new Exception("A meal is already assigned to that day");
        }
        return mealRepository.save(meal);
    }
    public Optional<Meal> findByMealDay(Constants.Days day){
        return mealRepository.findByMealDay(day);
    }

    public List<Meal> findAllByMealDayIsNull() {
        return mealRepository.findAllByMealDayIsNull();
    }
    public List<Meal> findAllByMealDayNotNull(){
        return mealRepository.findAllByMealDayNotNull();
    }
    public void saveAll(List<Meal> meals){
        mealRepository.saveAll(meals);
    }

    public void delete(Meal meal) {
        mealRepository.delete(meal);
    }
}
