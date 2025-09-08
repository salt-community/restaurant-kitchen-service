package com.example.restaurant.kitchen_service.service;

import com.example.restaurant.kitchen_service.exception.IngredientsMissingException;
import com.example.restaurant.kitchen_service.kafka.dto.PaymentAuthorizedEvent;
import com.example.restaurant.kitchen_service.model.Ingredient;
import com.example.restaurant.kitchen_service.model.Recipe;
import com.example.restaurant.kitchen_service.repository.IngredientRepository;
import com.example.restaurant.kitchen_service.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Recipe> getAllRecipesById(List<Integer> recipeIds) {
        return recipeRepository.findAllById(recipeIds);
    }

    public static HashMap<Long, String> toFoodPreparedFormat(List<Recipe> recipes) {
        HashMap<Long, String> processedFoods = new HashMap<>();
        for (Recipe recipe : recipes) {
            processedFoods.put(recipe.getId(), recipe.getName());
        }
        return processedFoods;
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

    @Transactional
    public List<Recipe> craftSeveralFoods(List<PaymentAuthorizedEvent.ReceivedRecipeDto> recipesFromEvent) {
        //Mapping the recipes from the DTO to a hashmap of <ID,Quantity>

        HashMap<Integer, Integer> recipesFromDto = new HashMap<>();

        for (PaymentAuthorizedEvent.ReceivedRecipeDto recipeDto : recipesFromEvent) {
            recipesFromDto.put(recipeDto.itemId(), recipeDto.quantity());
        }

        List<Integer> recipesToCraft = new ArrayList<>();

        for (Map.Entry<Integer, Integer> recipeWQuantity : recipesFromDto.entrySet()) {
            for (int i = 0; i < recipeWQuantity.getValue(); i++) {
                recipesToCraft.add(recipeWQuantity.getKey());
            }
        }

        List<Recipe> craftedRecipes = new ArrayList<>();
        for (Recipe recipe : getAllRecipesById(recipesToCraft)) {
            craftedRecipes.add(craftFood(recipe.getId()));
        }

        return craftedRecipes;
    }
}