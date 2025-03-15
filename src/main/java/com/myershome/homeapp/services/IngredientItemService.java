package com.myershome.homeapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myershome.homeapp.model.Ingredient;
import com.myershome.homeapp.model.IngredientItem;
import com.myershome.homeapp.repository.IngredientItemRepository;
import com.myershome.homeapp.repository.IngredientRepository;
import com.myershome.homeapp.services.Constants.Measurements;

@Service
public class IngredientItemService {

        @Autowired
        IngredientItemRepository ingredientItemRepository;
        @Autowired
        IngredientRepository ingredientRepository;

        public List<IngredientItem> getAll() {
                return ingredientItemRepository.findAll();
        }

        public void addTestIngredientItems() {
                Ingredient first = new Ingredient().builder().onShoppingList(false)
                                .ingName("First Ing")
                                .id(1L).build();
                Ingredient second = new Ingredient().builder()
                                .onShoppingList(false)
                                .ingName("Second Ing")
                                .id(2L).build();
                ingredientRepository.save(first);
                ingredientRepository.save(second);
                IngredientItem firstIngItem = new IngredientItem().builder()
                                .amount(1.25).ingredient(first)
                                .measurement(Measurements.TABLESPOON).build();
                IngredientItem secondIngItem = new IngredientItem().builder()
                                .amount(2.25).ingredient(second)
                                .measurement(Measurements.TEASPOON).build();
                ingredientItemRepository.save(firstIngItem);
                ingredientItemRepository.save(secondIngItem);
        }

        public void deleteAll(List<IngredientItem> ingredientItems) {
                ingredientItemRepository.deleteAll(ingredientItems);
        }

        public void saveAll(List<IngredientItem> ingredientItems) {
                ingredientItemRepository.saveAll(ingredientItems);
        }

        public void delete(IngredientItem ingredientItem) {
                ingredientItemRepository.save(ingredientItem);
                ingredientItemRepository.delete(ingredientItem);
                ingredientRepository.delete(ingredientItem.ingredient);
        }

        public void save(IngredientItem ingredientItem) {
                ingredientRepository.save(ingredientItem.ingredient);
                ingredientItemRepository.save(ingredientItem);
        }

        public IngredientItem createIngItem() {
                Ingredient ing = new Ingredient();
                ing.setIngName(" ");
                IngredientItem item = IngredientItem.builder()
                                .measurement(Measurements.NONE)
                                .amount(0.0)
                                .ingredient(ing).build();
                save(item);
                return item;
        }

        public List<IngredientItem> findAllById(List<Long> ids) {
                return ingredientItemRepository.findAllById(ids);
        }
}
