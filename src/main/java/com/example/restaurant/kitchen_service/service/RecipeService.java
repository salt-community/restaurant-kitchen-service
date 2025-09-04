package com.example.restaurant.kitchen_service.service;

import com.example.restaurant.kitchen_service.exception.IngredientsMissingException;
import com.example.restaurant.kitchen_service.model.Ingredient;
import com.example.restaurant.kitchen_service.model.Recipe;
import com.example.restaurant.kitchen_service.repository.IngredientRepository;
import com.example.restaurant.kitchen_service.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Transactional
    public Recipe craftFood(Long recipeId) {
        Recipe recipe = recipeRepository.findByIdWithIngredients(recipeId);

        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getAvailableQuantity() < 1) {
                throw new IngredientsMissingException(
                        "Not enough " + ingredient.getName() +
                                " (available: " + ingredient.getAvailableQuantity() + ")"
                );
            }
            ingredient.setAvailableQuantity(ingredient.getAvailableQuantity() - 1);
        }

        ingredientRepository.saveAll(recipe.getIngredients());
        return recipe;
    }
}
