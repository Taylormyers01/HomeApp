package com.myershome.homeapp.services;

import com.myershome.homeapp.model.Ingredient;
import com.myershome.homeapp.model.Meal;
import com.myershome.homeapp.repository.IngredientRepository;
import com.myershome.homeapp.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class MealService {

    @Autowired
    MealRepository mealRepository;
    @Autowired
    IngredientRepository ingredientRepository;

    public Meal save(Meal meal){
        Set<Ingredient> ingredientSet = new HashSet<>();
        if(!meal.getIngredientList().isEmpty()){
            for(Ingredient i: meal.getIngredientList()){
                if(i.getId() != null && ingredientRepository.findById(i.getId()).isPresent()){
                    ingredientSet.add(ingredientRepository.findById(i.getId()).get());
                }
                else{
                    ingredientSet.add(i);
                }
            }
        }
        meal.setIngredientList(ingredientSet);
        return mealRepository.save(meal);
    }

}
