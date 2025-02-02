package com.example.demo.recipe.recipeController;

import com.example.demo.*;
import com.example.demo.recipe.APiAndDtos.*;
import com.example.demo.recipe.Service.RecipeService;
import com.example.demo.recipe.recipeEntity.RecipeEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping("/recipeController")
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;
    private HttpHeaders createSecurityHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Content-Type-Options", "nosniff");
        headers.add("X-Frame-Options", "DENY");
        headers.add("X-XSS-Protection", "1; mode=block");
        return headers;
    }
    private final Logger logger = LoggerFactory.getLogger(RecipeController.class);
    @PostMapping("/saveRecipe") // @Valid indikerer at den skal bruke valideringsreglene som er spesifisert i objekt klassen
    public ResponseEntity<APiResponse<RecipeEntity>> saveRecipeEntity(@Valid @RequestBody RecipeEntity recipeEntity) {
        logger.info("Trying to save recipeEntity to DB: " + recipeEntity);
        RecipeEntity savedRecipe = recipeService.saveRecipe(recipeEntity);
        RecipeDTO recipeDTO = recipeMapper.toDto(savedRecipe);
        APiResponse response = new APiResponse("Recipe successfully saved", savedRecipe.getId(), recipeDTO);
        logger.info("Recipe saved successfully with ID: {}", savedRecipe.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(createSecurityHeaders())
                .body(response);
    }
    @GetMapping("/getAllRecipes")
    public ResponseEntity<APiResponse<PagedResponseDTO<RecipeDTO>>> getAllRecipes(Pageable pageable) {
        logger.info("Fetching all recipes with pagination. Page size: {}, Page number: {}", pageable.getPageSize(), pageable.getPageNumber());
        Page<RecipeDTO> recipePage = recipeService.getAllRecipe(pageable);
        PagedResponseDTO<RecipeDTO> pagedResponseDTO = PagedResponseDTO.fromPage(recipePage);
        APiResponse<PagedResponseDTO<RecipeDTO>> response = new APiResponse<>("Successfully retrieved recipes", null,  pagedResponseDTO);
        logger.info("Retrieved {} recipes form page {}", pageable.getPageSize(),pageable.getPageNumber());
        return ResponseEntity.ok()
                .headers(createSecurityHeaders())
                .body(response);
    }
    @GetMapping("/getAllRecipesSorted")
    public ResponseEntity<APiResponse<PagedResponseDTO<RecipeDTO>>> getAllREcipesSorted(@RequestParam(required = false) String cuisine,
                                                                                        @RequestParam(required = false) String sort, Pageable pageable) {
        logger.info("Fetching recipes with filters - cuisine: {}, sort: {}, Page size: {}, Page number: {}",cuisine, sort, pageable.getPageSize(), pageable.getPageNumber());
        Page<RecipeDTO> recipePage = recipeService.getAllRecipesSorted(cuisine, sort, pageable);
        PagedResponseDTO<RecipeDTO> pagedResponseDTO = PagedResponseDTO.fromPage(recipePage);
        APiResponse response = new APiResponse<>("Successfully retrieved recipes",null,pagedResponseDTO);
        logger.info("Retrieved {} recipes from page {}", pageable.getPageSize(), pageable.getPageNumber());
        return ResponseEntity.ok()
                .headers(createSecurityHeaders())
                .body(response);
    }
    @PutMapping("/putRecipeById/{id}")
    public ResponseEntity<APiResponse<RecipeEntity>> updateById(@Valid @PathVariable Long id, @RequestBody RecipeEntity recipeEntity) {
        logger.info("Updating recipe with ID: {}.",id);
        recipeEntity.setId(id); // setter id-en som ble hentet fra url
        RecipeEntity recipe = recipeService.updateByID(id,recipeEntity);
        APiResponse response1 = new APiResponse<>("Successfully updated recipe with ID : " + id + ".", id, recipe);
        logger.info("Successfully updated recipe with ID: {}", id);
        return ResponseEntity.ok()
                .headers(createSecurityHeaders())
                .body(response1);
    }
    @GetMapping("/getRecipeById/{id}")
    public ResponseEntity<APiResponse<RecipeDTO>> getRecipeById(@PathVariable Long id) {
        logger.info("Request received to fetch recipe by ID: {}", id);
        RecipeEntity recipe = recipeService.getById(id);
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);
        APiResponse<RecipeDTO> aPiResponse = new APiResponse<>("Successfully retrieved recipe", recipe.getId(), recipeDTO);
        logger.info("Successfully retrieved RecipeEntity with ID: {}", id);
        // security headers
        return ResponseEntity.ok()
                .headers(createSecurityHeaders())
                .body(aPiResponse);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<APiResponse<RecipeEntity>> deleteByID(@PathVariable Long id) {
        logger.info("Retrieved request for deleting recipeEntity with ID {}", id);
        recipeService.deleteRecipeById(id);
        logger.info("Successfully deleted recipeEntity with ID {}",id);
        APiResponse<RecipeEntity> apiResponse = new APiResponse<>("Successfully deleted recipeEntity",id,null);
        return ResponseEntity.ok()
                .headers(createSecurityHeaders())
                .body(apiResponse);
    }

    @DeleteMapping("/DeleteRecipiEntitiesByPreparationTimeANDDifficultyLevel/{preparationTime}/{difficultylevel}")
    public ResponseEntity<APiResponse<Object>> deleteRecipeEntitiesWithPreparationtime(
            @PathVariable("preparationTime") @Valid @Min(value = 1, message = "Preparation time must be a positive.") @NotNull int preparationtime,
            @PathVariable("difficultylevel") @NotNull(message = "Difficultylevel cannot be null") RecipeEntity.DifficultyLevel difficultyLevel, HttpServletRequest request) {
        logger.info("Deleting recipeEntities with preparationTime Greather than {} AND difficultyLevel like {}", preparationtime, difficultyLevel);
        int deletedRecipes = recipeService.deleteAllRecipesWithPreparationtimeGreatherThanEqualAndDifficultyLevelLike(preparationtime, difficultyLevel, request);
        logger.info("Successfully deleted all recipeEntities with preparationTime Greather than {} and difficultylevel like {}.", preparationtime, difficultyLevel);
        APiResponse apiresponse = new APiResponse("Successfully deleted " + deletedRecipes + " recipes.", null, null);
        return ResponseEntity.ok()
                .headers(createSecurityHeaders())
                .body(apiresponse);
    }

    @PutMapping("/updateRecipeEntitiesByCuisineTypeANDDifficultyLevel/{newCuisineType}/{newDifficultyLevel}/{oldCuisineType}/{oldDifficultyLevel}")
    public ResponseEntity<APiResponse<Integer>> updateRecipeEntitiesByCuisineTypeAndDifficultyLevel(
            @PathVariable("newCuisineType") @Valid @NotBlank String newCuisineType,
            @PathVariable("newDifficultyLevel") @NotNull(message = "Difficultylevel cannot be null")  RecipeEntity.DifficultyLevel newDifficultyLevel,
            @PathVariable("oldCuisineType") @Valid @NotBlank String oldCuisineType,
            @PathVariable("oldDifficultyLevel") @NotNull(message = "Difficultylevel cannot be null")  RecipeEntity.DifficultyLevel oldDifficultyLevel,
            HttpServletRequest request) {
        logger.info("Retrieved request for updating all recipe Entites by cusineType: {} and difficultyLevel: {}", oldCuisineType, oldDifficultyLevel);
        logger.info("Database will update them to newCuisineType: {} and newDifficultyLevel: {}.",newCuisineType, newDifficultyLevel);
        int updatedCount = recipeService.updateRecipeEntitiesByCuisineTypeAndDifficultyLevel(newCuisineType, newDifficultyLevel, oldCuisineType, oldDifficultyLevel, request);
        logger.info("Successfully updated {} recipe Entities.", updatedCount);
        APiResponse aPiResponse = new APiResponse("Successfully UPDATED " + updatedCount + " recipe Entities.", null, updatedCount);
        return ResponseEntity.ok()
                .headers(createSecurityHeaders())
                .body(aPiResponse);
    }

    @GetMapping("/employee/getRecipeStatistics")
    public ResponseEntity<APiResponse<RecipeStatsDTO>> getRecipeStatistics(@RequestParam Set<String> includeStats, HttpServletRequest request) {
        logger.info("Request for recipe statsitics recieved, including stats: {}.", includeStats);
        RecipeStatsDTO stats = recipeService.getRecipeStatistics(includeStats, request);
        logger.info("Successfully generated recipe statistics.");
        return ResponseEntity.ok()
                .headers(createSecurityHeaders())
                .body(new APiResponse<>("Successfully generated recipe statistics.", null, stats));
    }
}
