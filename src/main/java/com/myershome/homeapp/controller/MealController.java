package com.myershome.homeapp.controller;

import com.myershome.homeapp.model.Ingredient;
import com.myershome.homeapp.model.Meal;
import com.myershome.homeapp.repository.MealRepository;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.MealService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(Constants.APIResources.mealURL)
public class MealController {

    @Autowired
    MealRepository mealRepository;
    @Autowired
    MealService mealService;

    @GetMapping()
    public ResponseEntity<List<Meal>> getAllMeal(){
        return ResponseEntity.ok()
                .body(mealRepository.findAll());
    }

    @GetMapping("/add")
    public ResponseEntity<Meal> testing(){
        Meal meal = new Meal();
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("pasta");
        ingredient.setOnShoppingList(true);
        meal.setMealName("Lasagna");
        mealRepository.save(meal);
        return ResponseEntity.ok()
                .body(meal);
    }

    @PostMapping()
    public ResponseEntity<Meal> addMeal(@RequestBody @Valid Meal meal){
        Meal mealOut = mealService.save(meal);
        if(mealOut != null){
            return ResponseEntity.accepted().body(meal);
        }
        return ResponseEntity.badRequest()
                .body(null);
    }
}
