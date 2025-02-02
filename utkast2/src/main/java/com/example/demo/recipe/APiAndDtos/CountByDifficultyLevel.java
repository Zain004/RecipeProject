package com.example.demo.recipe.APiAndDtos;

import com.example.demo.recipe.recipeEntity.RecipeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CountByDifficultyLevel {
    private RecipeEntity.DifficultyLevel difficultyLevel;
    private Long Count;
}
