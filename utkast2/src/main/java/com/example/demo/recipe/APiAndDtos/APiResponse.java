package com.example.demo.recipe.APiAndDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class APiResponse<T> {
    private String message;
    private Long id;
    private T data;
}
