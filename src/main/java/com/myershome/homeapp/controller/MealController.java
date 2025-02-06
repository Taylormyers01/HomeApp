package com.myershome.homeapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myershome.homeapp.model.Ingredient;
import com.myershome.homeapp.model.Meal;
import com.myershome.homeapp.repository.MealRepository;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.MealService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(Constants.APIResources.mealURL)
public class MealController {

    @Autowired
    MealRepository mealRepository;
    @Autowired
    MealService mealService;

    @GetMapping()
    public ResponseEntity<List<Meal>> getAllMeal() {
        return ResponseEntity.ok()
                .body(mealRepository.findAll());
    }

    @GetMapping("/add")
    public ResponseEntity<Meal> testing() {
        Meal meal = new Meal();
        Ingredient ingredient = new Ingredient();
        ingredient.setIngName("pasta");
        ingredient.setOnShoppingList(true);
        meal.setMealName("Lasagna");
        mealRepository.save(meal);
        return ResponseEntity.ok()
                .body(meal);
    }

    @PostMapping()
    public ResponseEntity<Meal> addMeal(@RequestBody @Valid Meal meal) {
        try {
            Meal mealOut = mealService.save(meal);
            if (mealOut != null) {
                return ResponseEntity.accepted().body(meal);
            }
            return ResponseEntity.badRequest()
                    .body(null);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.accepted().body(meal);
        }
    }

    @PostMapping("/parse")
    public ResponseEntity<Meal> parseRecipe(@RequestBody @Valid String webpageUrl) {
        System.out.println("Webpage url: " + webpageUrl);
        Meal m = mealService.parseMeal(webpageUrl);
        return ResponseEntity.ok().body(m);
    }
}
