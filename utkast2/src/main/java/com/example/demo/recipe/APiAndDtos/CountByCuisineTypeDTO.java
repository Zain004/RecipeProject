package com.example.demo.recipe.APiAndDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CountByCuisineTypeDTO {
    private String cuisineType;
    private Long count;
}
