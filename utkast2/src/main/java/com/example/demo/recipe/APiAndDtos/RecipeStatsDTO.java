package com.example.demo.recipe.APiAndDtos;

import com.example.demo.recipe.recipeEntity.RecipeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeStatsDTO {
    private long totalRecipes;
    private Map<String, Long> recipesByCuisineType;
    private Map<RecipeEntity.DifficultyLevel, Long> recipesByDifficultyLevel;
    private Double averagePreparationTime;
}
