package com.example.demo.recipe.DataLoader;

import com.example.demo.recipe.recipeEntity.RecipeEntity;
import com.example.demo.recipe.recipeRepository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // component sier til Spring at denne klassen er ansvarlig for å opprette instanser av denne klassen
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RecipeRepository recipeRepository;
    @Override
    public void run(String... args) {
        if (recipeRepository.count() == 0) {
            List<RecipeEntity> recipes = List.of(
                    new RecipeEntity("Classic Margherita Pizza", "Dough, Tomato Sauce, Mozzarella, Basil", 30, RecipeEntity.DifficultyLevel.EASY, "Italian"),
                    new RecipeEntity("Spicy Chicken Curry", "Chicken, Coconut Milk, Spices, Rice", 45, RecipeEntity.DifficultyLevel.MEDIUM, "Indian"),
                    new RecipeEntity("Chocolate Chip Cookies", "Flour, Butter, Sugar, Chocolate Chips", 25, RecipeEntity.DifficultyLevel.EASY, "American"),
                    new RecipeEntity("Beef Stroganoff", "Beef, Mushrooms, Sour Cream, Noodles", 55, RecipeEntity.DifficultyLevel.HARD, "Russian"),
                    new RecipeEntity("Sushi Rolls", "Sushi Rice, Nori, Fish, Vegetables", 60, RecipeEntity.DifficultyLevel.HARD, "Japanese"),
                    new RecipeEntity("Tacos al Pastor", "Pork, Pineapple, Tortillas, Cilantro", 40, RecipeEntity.DifficultyLevel.MEDIUM, "Mexican"),
                    new RecipeEntity("French Onion Soup", "Onions, Broth, Bread, Cheese", 70, RecipeEntity.DifficultyLevel.MEDIUM, "French"),
                    new RecipeEntity("Pad Thai", "Rice Noodles, Tofu, Shrimp, Peanuts, Sauce", 35, RecipeEntity.DifficultyLevel.MEDIUM, "Thai"),
                    new RecipeEntity("Shepherds Pie", "Ground Lamb, Potatoes, Carrots, Peas", 80, RecipeEntity.DifficultyLevel.HARD, "British"),
                    new RecipeEntity("Greek Salad", "Tomatoes, Cucumber, Olives, Feta, Olive Oil", 15, RecipeEntity.DifficultyLevel.EASY, "Greek"),
                    new RecipeEntity("Chicken Stir-Fry", "Chicken, Vegetables, Soy Sauce, Noodles", 25, RecipeEntity.DifficultyLevel.EASY, "Italian"),
                    new RecipeEntity("Sushi Rolls", "Sushi Rice, Nori, Fvgvish, Vegetftftables", 609, RecipeEntity.DifficultyLevel.HARD, "Japanese")
            );
            recipeRepository.saveAll(recipes);
            System.out.println("Dummy data inserted successfully!");
        } else {
            System.out.println("Data already exists in database. Skipping data insertion.");
        }
    }


}