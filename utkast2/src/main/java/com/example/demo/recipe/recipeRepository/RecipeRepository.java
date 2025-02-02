package com.example.demo.recipe.recipeRepository;

import com.example.demo.recipe.APiAndDtos.CountByCuisineTypeDTO;
import com.example.demo.recipe.APiAndDtos.CountByDifficultyLevel;
import com.example.demo.recipe.recipeEntity.RecipeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
    Page<RecipeEntity> findAll(Specification<RecipeEntity> spec, Pageable pageable);
    @Transactional
    @Modifying
    @Query("delete from RecipeEntity r where r.preparationTime >= :preparationTime " +
            "AND r.difficultyLevel = :difficultyLevel")
    int deleteRecipeEntitiesByPreparationTimeGreaterThanEqualAndDifficultyLevelLike(
            @Param("preparationTime") int preparationTime,
            @Param("difficultyLevel") RecipeEntity.DifficultyLevel difficultyLevel);
    @Transactional
    @Modifying
    @Query("UPDATE RecipeEntity r SET r.cuisineType = :newCuisineType, r.difficultyLevel = :newDifficultyLevel " +
            "where r.cuisineType = :currentCusineType AND r.difficultyLevel = :currentDifficultyLevel")
    int updateRecipeEntitiesByCuisineTypeAndDifficultyLevel(@Param("newCuisineType") String newCuisineType, @Param("newDifficultyLevel") RecipeEntity.DifficultyLevel newDifficultyLevel,
                                                            @Param("currentCusineType") String currentCusineType, @Param("currentDifficultyLevel") RecipeEntity.DifficultyLevel currentDifficultyLevel);

    // blir ikke brukt, ekstra metode nedenfor
    @Query("SELECT r FROM RecipeEntity r where r.cuisineType = :cuisineType AND r.difficultyLevel = :difficultyLevel")
    List<RecipeEntity> findRecipeEntitiesByCuisineTypeAndDifficultyLevel(@Param("cuisineType") String cuisineType, @Param("difficultyLevel") RecipeEntity.DifficultyLevel difficultyLevel);

    // Nye metoder for statistikk - Database-effektive
    @Query("SELECT COUNT(r) FROM RecipeEntity r")
    long count(); // JPA sin innebygde Count() er enda bedre
    // Grupper statistikk etter cusineType
    @Query("SELECT new com.example.demo.recipe.APiAndDtos.CountByCuisineTypeDTO(r.cuisineType, COUNT(r))" +
            " FROM RecipeEntity r GROUP BY r.cuisineType")
    List<CountByCuisineTypeDTO> countByCuisineType();
    // Grupper statistikk etter difficultyLevel
    @Query("SELECT new com.example.demo.recipe.APiAndDtos.CountByDifficultyLevel(r.difficultyLevel, COUNT(r))" +
            " FROM RecipeEntity r " +
            "GROUP BY r.difficultyLevel " +
            "ORDER BY COUNT(r) DESC LIMIT 3")
    List<CountByDifficultyLevel> countByDifficultyLevel();
    // beregn gjennomsnittlig preparationTime
    @Query("SELECT AVG(r.preparationTime) FROM RecipeEntity r")
    Double getAveragePreparationTime();

}
