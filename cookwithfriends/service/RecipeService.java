package com.bee.cookwithfriends.service;

import com.bee.cookwithfriends.dto.recipe.*;
import com.bee.cookwithfriends.entity.Ingredient;
import com.bee.cookwithfriends.entity.Instruction;
import com.bee.cookwithfriends.entity.Recipe;
import com.bee.cookwithfriends.entity.RecipeIngredient;
import com.bee.cookwithfriends.repositories.IngredientRepository;
import com.bee.cookwithfriends.repositories.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final ModelMapper mapper;

    /**
     * Constructor for RecipeService.
     * @param recipeRepository The repository for handling recipe data.
     * @param mapper The ModelMapper for mapping entities to DTOs.
     * @param ingredientRepository The repository for handling ingredient data.
     */
    public RecipeService(RecipeRepository recipeRepository, ModelMapper mapper, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.mapper = mapper;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * Method to find a recipe by its ID.
     * @param id The ID of the recipe.
     * @return A RecipeDTO representing the found recipe.
     * @throws ResponseStatusException If the recipe is not found.
     */
    public RecipeDTO findById(int id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found")); // Find the recipe by ID

        System.out.println(recipe.getPreparationTime()); // Print the preparation time for debugging purposes

        RecipeDTO recipeDTO = mapper.map(recipe, RecipeDTO.class); // Map the recipe to a RecipeDTO

        recipeDTO.setIngredients(recipeIngredientListToDTO(recipe.getIngredients())); // Set the ingredients in the DTO

        return recipeDTO;
    }

    /**
     * Method to search for recipes by title.
     * @param title The title to search for.
     * @return A list of RecipeSmallDTO representing the found recipes.
     */
    public List<RecipeSmallDTO> searchByTitle(String title) {
        List<Recipe> recipes = recipeRepository.findByTitleContainingIgnoreCase(title); // Find recipes by title

        return recipeListToRecipeSmallDTOList(recipes); // Convert the list of recipes to a list of RecipeSmallDTO
    }

    /**
     * Method to get a random recipe.
     * @return A RecipeDTO representing the random recipe.
     * @throws ResponseStatusException If no recipe is found.
     */
    public RecipeDTO getRandomRecipe() {
        Recipe recipe = recipeRepository.getRandomRecipe().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Recipe found")); // Get a random recipe

        RecipeDTO recipeDTO = mapper.map(recipe, RecipeDTO.class); // Map the recipe to a RecipeDTO

        recipeDTO.setIngredients(recipeIngredientListToDTO(recipe.getIngredients())); // Set the ingredients in the DTO

        return recipeDTO;
    }

    /**
     * Method to get a stack of random recipes.
     * @param count The number of random recipes to retrieve.
     * @return A list of RecipeSmallDTO representing the random recipes.
     * @throws ResponseStatusException If no recipes are found.
     */
    public List<RecipeSmallDTO> getRandomStackOfRecipes(int count) {
        List<Recipe> recipes = recipeRepository.getRandomStackOfRecipes(count).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Recipes found")); // Get a stack of random recipes

        return recipeListToRecipeSmallDTOList(recipes); // Convert the list of recipes to a list of RecipeSmallDTO
    }

    /**
     * Method to add a new recipe.
     * @param recipeDTO The DTO containing the new recipe data.
     * @return A RecipeDTO representing the added recipe.
     */
    @Transactional
    public RecipeDTO addRecipe(AddRecipeDTO recipeDTO) {
        Recipe recipe = mapper.map(recipeDTO, Recipe.class); // Map the DTO to a Recipe entity

        recipe.setRating(0); // Initialize the rating
        recipe.setSumRating(0); // Initialize the sum of ratings
        recipe.setCountRating(0); // Initialize the count of ratings

        for (Instruction instruction : recipe.getInstructions()) {
            instruction.setRecipe(recipe); // Set the recipe for each instruction
        }

        List<RecipeIngredient> ingredients = new ArrayList<>();
        for (RecipeIngredientDTO ingredientDTO : recipeDTO.getRecipeIngredients()) {
            RecipeIngredient recipeIngredient = createRecipeIngredient(ingredientDTO); // Create a RecipeIngredient
            recipeIngredient.setRecipe(recipe); // Set the recipe for the ingredient
            ingredients.add(recipeIngredient); // Add the ingredient to the list
        }

        recipe.setIngredients(ingredients); // Set the list of ingredients for the recipe

        recipe = recipeRepository.save(recipe); // Save the recipe to the repository

        return mapper.map(recipe, RecipeDTO.class); // Map the saved recipe to a RecipeDTO
    }

    /**
     * Method to rate a recipe.
     * @param rateDTO The DTO containing the rating data.
     * @return The new rating value.
     * @throws ResponseStatusException If the recipe is not found.
     */
    @Transactional
    public int rateRecipe(RateRecipeDTO rateDTO) {
        Recipe recipe = recipeRepository.findById(rateDTO.getRecipeId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found")); // Find the recipe by ID

        if (rateDTO.getRating() > 10 || rateDTO.getRating() < 0) return recipe.getRating(); // Check if the rating is within the valid range

        recipe.setSumRating(recipe.getSumRating() + rateDTO.getRating()); // Update the sum of ratings
        recipe.setCountRating(recipe.getCountRating() + 1); // Update the count of ratings

        recipe.setRating(recipe.getSumRating() / recipe.getCountRating()); // Calculate the new rating

        recipe = recipeRepository.save(recipe); // Save the updated recipe

        return recipe.getRating(); // Return the new rating
    }

    /**
     * Method to convert a list of recipes to a list of RecipeSmallDTO.
     * @param recipes The list of recipes.
     * @return A list of RecipeSmallDTO.
     */
    private List<RecipeSmallDTO> recipeListToRecipeSmallDTOList(List<Recipe> recipes) {
        List<RecipeSmallDTO> recipeDTOs = new ArrayList<>();

        for (Recipe recipe : recipes) {
            recipeDTOs.add(mapper.map(recipe, RecipeSmallDTO.class)); // Map each recipe to a RecipeSmallDTO
        }

        return recipeDTOs;
    }

    /**
     * Method to create a RecipeIngredient from a RecipeIngredientDTO.
     * @param ingredientDTO The DTO containing the ingredient data.
     * @return A RecipeIngredient entity.
     */
    private RecipeIngredient createRecipeIngredient(RecipeIngredientDTO ingredientDTO) {
        Ingredient ingredient = findOrCreateIngredient(ingredientDTO); // Find or create the ingredient
        return buildRecipeIngredient(ingredient, ingredientDTO); // Build the RecipeIngredient
    }

    /**
     * Method to find or create an ingredient.
     * @param ingredientDTO The DTO containing the ingredient data.
     * @return An Ingredient entity.
     */
    private Ingredient findOrCreateIngredient(RecipeIngredientDTO ingredientDTO) {
        return ingredientRepository.findByName(ingredientDTO.getName())
                .orElseGet(() -> saveNewIngredient(ingredientDTO)); // Find the ingredient by name or create a new one
    }

    /**
     * Method to save a new ingredient.
     * @param ingredientDTO The DTO containing the ingredient data.
     * @return The saved Ingredient entity.
     */
    private Ingredient saveNewIngredient(RecipeIngredientDTO ingredientDTO) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDTO.getName()); // Set the name of the ingredient
        ingredient.setStandardUnit(ingredientDTO.getUnit()); // Set the standard unit of the ingredient
        return ingredientRepository.save(ingredient); // Save the new ingredient
    }

    /**
     * Method to build a RecipeIngredient.
     * @param ingredient The Ingredient entity.
     * @param ingredientDTO The DTO containing the ingredient data.
     * @return A RecipeIngredient entity.
     */
    private RecipeIngredient buildRecipeIngredient(Ingredient ingredient, RecipeIngredientDTO ingredientDTO) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setIngredient(ingredient); // Set the ingredient
        recipeIngredient.setUnit(ingredientDTO.getUnit()); // Set the unit
        recipeIngredient.setAmount(ingredientDTO.getAmount()); // Set the amount
        return recipeIngredient;
    }

    /**
     * Method to convert a list of RecipeIngredient entities to a list of RecipeIngredientDTO.
     * @param recipeIngredients The list of RecipeIngredient entities.
     * @return A list of RecipeIngredientDTO.
     */
    private List<RecipeIngredientDTO> recipeIngredientListToDTO(List<RecipeIngredient> recipeIngredients) {
        List<RecipeIngredientDTO> recipeIngredientDTOList = new ArrayList<>();

        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            RecipeIngredientDTO ingredientDTO = new RecipeIngredientDTO();
            ingredientDTO.setAmount(recipeIngredient.getAmount()); // Set the amount
            ingredientDTO.setUnit(recipeIngredient.getUnit()); // Set the unit

            Ingredient ingredient = ingredientRepository.findById(recipeIngredient.getIngredient().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found!")); // Find the ingredient by ID
            ingredientDTO.setName(ingredient.getName()); // Set the name of the ingredient

            recipeIngredientDTOList.add(ingredientDTO); // Add the DTO to the list
        }

        return recipeIngredientDTOList;
    }
}