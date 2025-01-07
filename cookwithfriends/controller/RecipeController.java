package com.bee.cookwithfriends.controller;

import com.bee.cookwithfriends.dto.recipe.AddRecipeDTO;
import com.bee.cookwithfriends.dto.recipe.RateRecipeDTO;
import com.bee.cookwithfriends.dto.recipe.RecipeSmallDTO;
import com.bee.cookwithfriends.dto.recipe.RecipeDTO;
import com.bee.cookwithfriends.service.ImageService;
import com.bee.cookwithfriends.service.RecipeService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Controller for handling recipe-related endpoints.
 */
@RequestMapping("/recipe")
@RestController
public class RecipeController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    /**
     * Constructor for RecipeController.
     * @param recipeService The service for handling recipe operations.
     * @param imageService The service for handling image operations.
     */
    public RecipeController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    /**
     * Endpoint to get a random recipe.
     * @return A ResponseEntity containing the random recipe.
     */
    @GetMapping("/random")
    public ResponseEntity<RecipeDTO> getRandomRecipe() {
        return ResponseEntity.ok(recipeService.getRandomRecipe());
    }

    /**
     * Endpoint to get a stack of random recipes.
     * @param count The number of random recipes to retrieve.
     * @return A ResponseEntity containing a list of random recipes.
     */
    @GetMapping("/random/{count}")
    public ResponseEntity<List<RecipeSmallDTO>> getRandomStackOfRecipes(@PathVariable int count) {
        return ResponseEntity.ok(recipeService.getRandomStackOfRecipes(count));
    }

    /**
     * Endpoint to get a recipe by its ID.
     * @param id The ID of the recipe.
     * @return A ResponseEntity containing the recipe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(recipeService.findById(id));
    }

    /**
     * Endpoint to search for recipes by title.
     * @param title The title to search for.
     * @return A ResponseEntity containing a list of matching recipes.
     */
    @GetMapping("/search")
    public ResponseEntity<List<RecipeSmallDTO>> searchRecipes(@RequestParam String title) {
        return ResponseEntity.ok(recipeService.searchByTitle(title));
    }

    /**
     * Endpoint to add a new recipe.
     * @param recipeDTO The DTO containing the new recipe data.
     * @return A ResponseEntity containing the added recipe.
     */
    @PostMapping("/add")
    public ResponseEntity<RecipeDTO> addRecipe(@RequestBody AddRecipeDTO recipeDTO) {
        return ResponseEntity.ok(recipeService.addRecipe(recipeDTO));
    }

    /**
     * Endpoint to rate a recipe.
     * @param ratingDTO The DTO containing the rating data.
     * @return A ResponseEntity containing the new rating value.
     */
    @PostMapping("/rate")
    public ResponseEntity<Integer> rateRecipe(@RequestBody RateRecipeDTO ratingDTO) {
        int newRating = recipeService.rateRecipe(ratingDTO); // Get the new rating after the update
        return ResponseEntity.ok(newRating);
    }

    /**
     * Endpoint to upload an image for a recipe.
     * @param file The image file to upload.
     * @param id The ID of the recipe.
     * @return A ResponseEntity indicating the success or failure of the upload.
     */
    @PostMapping("/image/{id}")
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file, @PathVariable int id) {
        try {
            imageService.storeImage(file, id); // Store the image in the service
            return new ResponseEntity<>("Image is uploaded", HttpStatus.OK);
        } catch (IOException exception) {
            return new ResponseEntity<>("Image is not uploaded", HttpStatus.OK); // Return a success status even if the upload fails (consider changing to HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Endpoint to get an image for a recipe.
     * @param id The ID of the recipe.
     * @return A ResponseEntity containing the image file.
     */
    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable int id) {
        Resource file = imageService.loadFile(id); // Load the image file from the service
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream")) // Set the content type to octet-stream (binary data)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"") // Set the content disposition header for file download
                .body(file); // Return the file as the response body
    }
}
