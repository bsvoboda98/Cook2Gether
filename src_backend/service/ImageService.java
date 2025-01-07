package com.bee.cookwithfriends.service;

import com.bee.cookwithfriends.config.ImageConfig;
import com.bee.cookwithfriends.entity.Recipe;
import com.bee.cookwithfriends.repositories.RecipeRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {
    private final Path rootLocation;
    private final RecipeRepository recipeRepository;

    /**
     * Constructor for ImageService.
     * @param imageConfig The configuration for image storage.
     * @param recipeRepository The repository for handling recipe data.
     */
    public ImageService(ImageConfig imageConfig, RecipeRepository recipeRepository) {
        this.rootLocation = imageConfig.getImageStoragePath(); // Get the root location for image storage
        this.recipeRepository = recipeRepository;
    }

    /**
     * Method to store an image for a recipe.
     * @param file The image file to store.
     * @param recipeId The ID of the recipe.
     * @throws IOException If the file cannot be stored.
     */
    public void storeImage(MultipartFile file, int recipeId) throws IOException {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found")); // Find the recipe by ID

        String filename = getFilename(recipe); // Generate the filename for the image
        String originalFileName = file.getOriginalFilename(); // Get the original filename of the uploaded file

        assert originalFileName != null;
        int dotIndex = originalFileName.lastIndexOf('.'); // Find the last dot in the original filename to get the file extension

        String fileExtension = "";
        if (dotIndex > 0) {
            fileExtension = file.getOriginalFilename().substring(dotIndex); // Extract the file extension
        }

        try {
            Files.copy(file.getInputStream(), rootLocation.resolve(filename + fileExtension), StandardCopyOption.REPLACE_EXISTING); // Copy the file to the storage location
        } catch (IOException exception) {
            throw new IOException("Could not store image for recipe " + recipe.getTitle() + ". Please try again!", exception); // Throw an exception if the file cannot be stored
        }
    }

    /**
     * Method to load an image for a recipe.
     * @param recipeId The ID of the recipe.
     * @return A Resource representing the image file.
     */
    public Resource loadFile(int recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found")); // Find the recipe by ID
        String filename = getFilename(recipe); // Generate the filename for the image

        try {
            System.out.println(filename); // Print the filename for debugging purposes

            Path existingFilePath = getPathWithFileExtension(filename); // Get the path to the image file with the correct extension

            if (existingFilePath == null) {
                throw new RuntimeException("No matching file found for recipe ID: " + recipeId); // Throw an exception if no matching file is found
            }

            return new FileSystemResource(existingFilePath); // Return the file as a Resource

        } catch (Exception exception) {
            throw new RuntimeException("Error: " + exception.getMessage(), exception); // Throw an exception if there is an error
        }
    }

    /**
     * Method to get the path to a file with the correct extension.
     * @param filename The base filename.
     * @return The path to the file with the correct extension.
     */
    private Path getPathWithFileExtension(String filename) {
        String[] extensions = {".png", ".jpg"}; // Supported file extensions

        Path existingFilePath = null;

        for (String ext : extensions) {
            Path filePathWithExtension = rootLocation.resolve(filename + ext); // Create the path with the current extension

            Resource resource = new FileSystemResource(filePathWithExtension); // Create a Resource for the file

            if (resource.exists() && resource.isReadable()) { // Check if the file exists and is readable
                existingFilePath = filePathWithExtension; // Set the existing file path
            }
        }
        return existingFilePath; // Return the path to the file with the correct extension
    }

    /**
     * Method to generate the filename for a recipe image.
     * @param recipe The recipe for which to generate the filename.
     * @return The generated filename.
     */
    private String getFilename(Recipe recipe) {
        return recipe.getId().toString() + "_" + recipe.getTitle(); // Generate the filename using the recipe ID and title
    }
}