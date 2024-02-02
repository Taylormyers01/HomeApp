package com.myershome.homeapp.controller;

import com.myershome.homeapp.model.Ingredient;
import com.myershome.homeapp.repository.IngredientRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.myershome.homeapp.services.Constants.APIResources;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(APIResources.ingredientURL)
public class IngredientController
{
    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping()
    public List<Ingredient> getIngredient(){
        return ingredientRepository.findAll();
    }

    @PostMapping()
    public ResponseEntity<Ingredient> postIngredient(@Valid @RequestBody Ingredient ingredient){
        if(ingredient.getId() == null){
            return ResponseEntity.ok()
                    .body(ingredientRepository.save(ingredient));
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Long id){
        Optional<Ingredient> result = ingredientRepository.findById(id);
        return result.map(ingredient -> ResponseEntity.ok()
                .body(ingredient)).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }
}  