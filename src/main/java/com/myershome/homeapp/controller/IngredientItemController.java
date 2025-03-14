package com.myershome.homeapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myershome.homeapp.model.IngredientItem;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.IngredientItemService;

@RestController
@RequestMapping(Constants.APIResources.ingredientItem)
public class IngredientItemController {

    @Autowired
    IngredientItemService service;

    @GetMapping()
    public ResponseEntity<List<IngredientItem>> getAllIngredientItem() {

        return ResponseEntity.ok()
                .body(service.getAll());
    }

    @GetMapping("/add")
    public ResponseEntity<String> addTest() {

        service.addTestIngredientItems();
        return ResponseEntity.ok().body("Testing");
        // Ingredient i = Ingredient
    }
}
