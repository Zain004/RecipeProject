package com.example.demo.recipe.APiAndDtos;

import com.example.demo.recipe.recipeEntity.RecipeEntity;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper {
    public RecipeDTO toDto(RecipeEntity entity) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(entity.getId());
        dto.setRecipeName(entity.getRecipeName());
        dto.setIngredients(entity.getIngredients());
        dto.setPreparationTime(entity.getPreparationTime());
        dto.setDifficultyLevel(entity.getDifficultyLevel().toString());
        dto.setCuisineType(entity.getCuisineType());
        return dto;
    }
}
