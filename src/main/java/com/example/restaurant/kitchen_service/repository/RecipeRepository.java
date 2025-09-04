package com.example.restaurant.kitchen_service.repository;

import com.example.restaurant.kitchen_service.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}
