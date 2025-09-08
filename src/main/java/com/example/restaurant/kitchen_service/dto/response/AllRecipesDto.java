package com.example.restaurant.kitchen_service.dto.response;

public record AllRecipesDto(Long id, String name, String description, Boolean available, Integer availableAmount) {
}
