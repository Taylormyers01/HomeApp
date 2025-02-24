package com.myershome.homeapp.webapp.renderers;

import com.myershome.homeapp.services.IngredientItemService;
import com.myershome.homeapp.services.MealService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MealRendererFactory {

    IngredientItemService ingredientItemService;
    MealService mealService;
    private static final Logger LOG = LoggerFactory.getLogger(MealRendererFactory.class);



}
