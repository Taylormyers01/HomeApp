package com.myershome.homeapp.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.javaparser.utils.Log;
import com.myershome.homeapp.model.Ingredient;
import com.myershome.homeapp.model.IngredientItem;
import com.myershome.homeapp.model.Meal;
import com.myershome.homeapp.repository.IngredientRepository;
import com.myershome.homeapp.repository.MealRepository;

@Service
public class MealService {

    private static final Logger LOG = LoggerFactory.getLogger(MealService.class);

    @Autowired
    MealRepository mealRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    IngredientItemService ingredientItemService;

    public Meal save(Meal meal) throws Exception {
        if (meal.getMealDay() != null) {
            LOG.info("Made it to check meal");
            Optional<Meal> checkMeal = mealRepository.findByMealDay(meal.getMealDay());
            if (checkMeal.isPresent() && checkMeal.get().getId() != meal.getId()) {
                LOG.error("A meal is already assigned to day" + meal.getMealDay() + " for Meal Name: "
                        + meal.getMealName());
                throw new Exception("A meal is already assigned to that day");
            }

        }
        Meal output = mealRepository.save(meal);

        return output;
    }

    public Optional<Meal> findByMealDay(Constants.Days day) {
        return mealRepository.findByMealDay(day);
    }

    public List<Meal> findAllByMealDayIsNull() {
        return mealRepository.findAllByMealDayIsNull();
    }

    public List<Meal> findAllByMealDayNotNull() {
        return mealRepository.findAllByMealDayNotNull();
    }

    public void saveAll(List<Meal> meals) {
        mealRepository.saveAll(meals);
    }

    public void delete(Meal meal) {
        mealRepository.delete(meal);
    }

    public Meal parseMeal(String webpageString) {

        RecipeParser rp = new RecipeParser(webpageString);
        try {
            rp.generate();
            if (rp.ingredientObj != null) {
                rp.ingredientObj.forEach(i -> {
                    Optional<Ingredient> output = ingredientRepository.findByIngName(i.ingredient.getIngName());
                    if (output.isPresent()) {
                        i.ingredient = output.get();
                    } else {
                        ingredientRepository.save(i.ingredient);
                    }
                });
                ingredientItemService.saveAll(rp.ingredientObj);
            }
            String directions = "";
            for (String s : rp.instructions) {
                directions += s + "\n";
            }
            directions = directions.strip();
            Meal m = Meal.builder().mealName("")
                    .directions(directions).ingredientItems(rp.ingredientObj)
                    .recipeUrl(webpageString).build();
            // LOG.debug("Created meal " + m);
            return m;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public Meal removeIngItem(IngredientItem ingredientItem, Meal meal) {
        if (meal.getIngredientItems().contains(ingredientItem)) {
            List<IngredientItem> ingItemList = meal.getIngredientItems();
            ingItemList.remove(ingredientItem);
            meal.setIngredientItems(ingItemList);
            meal = mealRepository.save(meal);
            // ingredientItemService.delete(ingredientItem);
        }
        return meal;
    }
}
