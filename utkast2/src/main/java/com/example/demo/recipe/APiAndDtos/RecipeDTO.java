package com.example.demo.recipe.APiAndDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {
    private Long id;
    private String recipeName;
    private String ingredients;
    private int preparationTime;
    private String difficultyLevel;
    private String cuisineType;

}
