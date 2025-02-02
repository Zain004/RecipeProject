package com.example.demo.recipe.Service;

import com.example.demo.Authentication.Service.UserService;
import com.example.demo.Authentication.UserEntity.User;
import com.example.demo.recipe.APiAndDtos.CountByCuisineTypeDTO;
import com.example.demo.recipe.APiAndDtos.CountByDifficultyLevel;
import com.example.demo.recipe.APiAndDtos.RecipeDTO;
import com.example.demo.recipe.APiAndDtos.RecipeStatsDTO;
import com.example.demo.recipe.recipeEntity.RecipeEntity;
import com.example.demo.recipe.recipeRepository.RecipeRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // genererer kun for final
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final Logger logger = LoggerFactory.getLogger(RecipeService.class);
    private final UserService userService;
    @Transactional
    public RecipeEntity saveRecipe(@Valid RecipeEntity recipeEntity) {
        Optional.ofNullable(recipeEntity)
                .orElseThrow(() -> {
                    logger.warn("Attempted to save a null recipe.");
                    return new IllegalArgumentException("Recipe cannot be null.");
                });
    return recipeRepository.save(recipeEntity);
    }


    public Page<RecipeDTO> getAllRecipe(Pageable pageable) {
        logger.info("Fetching all recipes with pagination {}", pageable);
        Page<RecipeEntity> recipePage = recipeRepository.findAll(pageable);
        logger.info("Found {} recipes on page {}.", recipePage.getSize(), pageable.getPageNumber());
        return recipePage.map(this::convertDTO);

    }
    private RecipeDTO convertDTO(RecipeEntity recipeEntity) {
        return new RecipeDTO(
                recipeEntity.getId(),
                recipeEntity.getRecipeName(),
                recipeEntity.getIngredients(),
                recipeEntity.getPreparationTime(),
                recipeEntity.getDifficultyLevel().toString(),
                recipeEntity.getCuisineType());
    }

    public Page<RecipeDTO> getAllRecipesSorted(String cuisineType, String sort, Pageable pageable) {
        Specification<RecipeEntity> spec = createSpesification(cuisineType, sort);
        logger.info("Fetching all recipes with pagination {}", pageable);
        Page<RecipeEntity> recipePage = recipeRepository.findAll(spec, pageable);
        logger.info("Found {} recipes on Page {}.",recipePage.getSize(),pageable.getPageNumber());
        return recipePage.map(this::convertDTO);
    }
    private Specification<RecipeEntity> createSpesification(String cuisine, String sort) {
        // root = entitet, query = spørring, criteriaBuilder = lage predikat test objekt
        return (root, query, criteriaBuilder) -> { // standard måte å bruke Spesification på
            List<Predicate> predicates = new ArrayList<>(); // predicate er en representasjon av et enkelt søkekriterium
            if (cuisine != null && !cuisine.isEmpty()) { // utføres ikke hvis false
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("cuisineType")), "%" + cuisine.toLowerCase() + "%"));
            }
            if (sort != null && !sort.isEmpty()) {
                if (sort.equalsIgnoreCase("recipeName")) {
                    query.orderBy(criteriaBuilder.asc(root.get("recipeName")));
                } else {
                    throw new IllegalArgumentException("Invalid sorting parameter");
                }
            }
// Hvis sort er null eller tom, gjør ingenting – returner som den er
            // til slutt utføres filtrering og sortering
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    @Transactional
    public RecipeEntity updateByID(Long id, RecipeEntity recipeEntity) {
        logger.info("recieved request for updating by ID: ", id);
         Optional.ofNullable(recipeEntity)
                 .orElseThrow(() -> {
                     logger.warn("Attempted to update recipe with null body.");
                     return new IllegalArgumentException("Recipe cannot be null");
                 });
         Optional.ofNullable(id)
                         .orElseThrow(() -> {
                             logger.warn("Attempted to update with ID null");
                             return new IllegalArgumentException("Id cannot be null");
                         });

        logger.info("Updating recipe with id {}", id);
        return recipeRepository.findById(id)
                        .map((existingRecipe) -> {
                            BeanUtils.copyProperties(recipeEntity, existingRecipe,"id");
                            return recipeRepository.save(existingRecipe);
                        }).orElseThrow( () -> {
                                logger.warn("Recipe with Id {} not found.", id);
                                return new IllegalArgumentException("Recipe with Id " + id + " not found.");
                        });
    }
    public RecipeEntity getById(Long id) {
        return findRecipeById(id);
    }
    private RecipeEntity findRecipeById(Long id) {
        logger.info("Getting recipe by ID: {}",id);
        return recipeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No RecipeEntity found with ID: {}", id);
                    return new IllegalArgumentException("No recipeEntity found with ID: " + id);
                });
    }
    public void deleteRecipeById(Long id) {
        logger.info("Attemted to delete recipe by id. ");
        // sjekker om id er null
        Optional.ofNullable(id)
                .orElseThrow(() -> {
                    logger.warn("Attempted to delete with ID null.");
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST,"Recipe ID cannot be null.");
                });
        // sjekker om id eksisterer
        if(!recipeRepository.existsById(id)) {
            logger.warn("RecipeEntity with ID {} NOT FOUND", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RecipeEntity with ID " + id + " NOT FOUND");
        }
        logger.info("Found recipeEntity with ID {}", id);
        recipeRepository.deleteById(id);
        logger.info("Successfully deleted recipeEntity with ID {}", id);
    }
    @Transactional
    public int deleteAllRecipesWithPreparationtimeGreatherThanEqualAndDifficultyLevelLike(int preparationTime, RecipeEntity.DifficultyLevel difficultyLevel, HttpServletRequest request) {
        logger.info("Retrieved request for deleting Recipe Entities with preparation time greather" +
                "than {} and difficulty level like {}", preparationTime, difficultyLevel);
        validateInput(difficultyLevel, "difficultyLevel");
        validatePositiveInteger(preparationTime, "preparationTime");
        logger.info("Checking if user is signed in");
        userService.authenticate(request);
        int deletedCount = recipeRepository.deleteRecipeEntitiesByPreparationTimeGreaterThanEqualAndDifficultyLevelLike(preparationTime, difficultyLevel);
        logger.info("Finally deleted {} recipeEntites.", deletedCount);
        return deletedCount;
    }

    private void validateInput(Object input, String fieldName) {
        logger.info("Validating input: {} with fieldName: {}", input, fieldName);
        Optional.ofNullable(input)
                .orElseThrow(() -> {
                    logger.warn("{} cannot be null.", fieldName);
                    return new IllegalArgumentException(fieldName + " cannot be null.");
                });
    }
    private void validatePositiveInteger(int input, String fieldName) {
        logger.info("Validating input: {} with fieldName: {}", input, fieldName);
        if (input <= 0) {
            logger.warn("{} must be a positive integer.", fieldName);
            throw new IllegalArgumentException(fieldName + " must be a positive integer.");
        }
    }


    public int updateRecipeEntitiesByCuisineTypeAndDifficultyLevel(String newCuisineType, RecipeEntity.DifficultyLevel newDifficultyLevel,
                                                                   String oldCuisineType, RecipeEntity.DifficultyLevel oldDifficultyLevel, HttpServletRequest request) {
        logger.info("Recieved request to update recipe Entities by CuisineType: {} AND difficultyLevel: {}");
        logger.info("Checking if User is logged in and admin.");
        userService.authenticate(request);
        validateInput(newCuisineType, "cuisineType");
        validateInput(newDifficultyLevel, "difficultyLevel");
        validateInput(oldCuisineType, "cuisineType");
        validateInput(oldDifficultyLevel, "difficultyLevel");
        int updatedCount = recipeRepository.updateRecipeEntitiesByCuisineTypeAndDifficultyLevel(newCuisineType, newDifficultyLevel, oldCuisineType, oldDifficultyLevel);
        logger.info("Successfully updated {} recipe Entities", updatedCount);
        return updatedCount;
    }

    public RecipeStatsDTO getRecipeStatistics(Set<String> includeStats, HttpServletRequest request) {
        logger.info("Recieved request for generating statistics.");
        logger.info("Checking if user is signed in and is Admin.");
        userService.authenticate(request);
        RecipeStatsDTO statsDTO = new RecipeStatsDTO();
        // hvis forespørselen kommer tom bakfra, indikerer det at brukeren vil ha alt statistikk
        // returnerer antall elementer
        if(includeStats == null || includeStats.isEmpty() || includeStats.contains("totalRecipes")) {
            statsDTO.setTotalRecipes(recipeRepository.count());
            logger.info("Successfully counted recipeEntities");
        }
        // returnerer set av cuisineType med antall
        if(includeStats == null || includeStats.isEmpty() || includeStats.contains("recipesByCuisineType")) {
            List<CountByCuisineTypeDTO> results = recipeRepository.countByCuisineType();
            Map<String, Long> liste = results.stream()
                            .collect(Collectors.toMap(CountByCuisineTypeDTO::getCuisineType, CountByCuisineTypeDTO::getCount));
            statsDTO.setRecipesByCuisineType(liste);
            logger.info("Successfully mapped cuisineTypes");
        }
        // Hent statsistikk gruppert etter difficultyLevel
        if(includeStats == null || includeStats.isEmpty() || includeStats.contains("recipesByDifficultyLevel")){
            List<CountByDifficultyLevel> results = recipeRepository.countByDifficultyLevel();
            Map<RecipeEntity.DifficultyLevel, Long> liste = results.stream()
                            .collect(Collectors.toMap(CountByDifficultyLevel::getDifficultyLevel, CountByDifficultyLevel::getCount));
            statsDTO.setRecipesByDifficultyLevel(liste);
            logger.info("Successfully grouped statistics by difficultyLevel");
        }
        // henter gjennomsnittlig forberedelsesTid
        if(includeStats == null || includeStats.isEmpty() || includeStats.contains("averagePreparationTime")){
            Double averageTime = recipeRepository.getAveragePreparationTime();
            statsDTO.setAveragePreparationTime(averageTime != null ? averageTime : 0.0);
            logger.info("Successfully recieved from DB Average Preparation Time.");
        }
        logger.info("Statistics calculated: {}.", statsDTO);
        return statsDTO;
    }
}
