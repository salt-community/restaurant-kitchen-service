package com.example.restaurant.kitchen_service.service;

import com.example.restaurant.kitchen_service.model.Ingredient;
import com.example.restaurant.kitchen_service.model.Recipe;
import com.example.restaurant.kitchen_service.repository.IngredientRepository;
import com.example.restaurant.kitchen_service.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    private  RecipeRepository recipeRepository;
    private  IngredientRepository ingredientRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public boolean isRecipeCrafteable(Long recipeId){
        Recipe recipe = recipeRepository.getRecipeById((recipeId));
        List<Ingredient> ingredientList = recipe.getIngredients();
        return ingredientList.stream().allMatch((ingredient -> ingredient.getAvailableQuantity()>=1));
    }
}
