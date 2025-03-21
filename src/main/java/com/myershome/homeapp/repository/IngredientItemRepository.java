package com.myershome.homeapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myershome.homeapp.model.IngredientItem;

public interface IngredientItemRepository extends JpaRepository<IngredientItem, Long> {

}