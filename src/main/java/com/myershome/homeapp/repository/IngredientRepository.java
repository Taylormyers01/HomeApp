package com.myershome.homeapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myershome.homeapp.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByIngName(String ingName);
}
