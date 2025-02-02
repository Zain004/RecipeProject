package com.example.demo.recipe.recipeEntity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Builder // for å teste å bygge objekt
@AllArgsConstructor // @Builder trenger en allargsConstructor
@EntityListeners(AuditingEntityListener.class) // bruker ferdig lagd kalsse for å holde styr på endringer
@Entity
@Table(
        name = "recipe_entity",
        indexes = {
                @Index(name = "idx_recipe_name", columnList = "recipe_name"),
                @Index(name = "idx_cuisine_type", columnList = "cuisine_type")
        }
)
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 50, message = "Recipe name must be between 2 and 50 characters")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s{2})[A-Za-z.\\- ]{2,50}(?<!\\s)$", message = "Recipe name must contain only letters, spaces, dots, and dashes, and no leading or trailing spaces.")
    @Column(name = "recipe_name", nullable = false, length = 50)
    private String recipeName;

    @NotBlank
    @Lob
    @Column(name = "ingredients", columnDefinition = "LongText", nullable = false)
    private String ingredients;

    @Positive(message = "Preparation time must be a positive number")
    @NotNull // bruker denne istedenfor Notblank på int, fordi notblank er for String
    @Column(name = "preparaiton_time", nullable = false)
    private int preparationTime;

    @NotNull(message = "Difficulty level is required")
    @Enumerated(EnumType.STRING) // For enum, lagres som en String
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;

    @NotBlank
    @Size(min = 2, max = 50, message = "Cuisine type must be between 2 and 50 characters")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s{2})[A-Za-z .\\-]+(?<!\\s)$", message = "Cuisine type must contain only letters, spaces, dots, and dashes, and no leading or trailing spaces.")
    @Column(name = "cuisine_type", nullable = false, length = 50)
    private String cuisineType;

    public RecipeEntity(String recipeName, String ingredients, int preparationTime, DifficultyLevel difficultyLevel, String cuisineType) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.preparationTime = preparationTime;
        this.difficultyLevel = difficultyLevel;
        this.cuisineType = cuisineType;
    }
    @CreatedDate
    @Column(name = "Created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    }
}

