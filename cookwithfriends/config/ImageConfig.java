package com.bee.cookwithfriends.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ImageConfig {

    @Value("${image.storage.path}")
    private String imageStoragePath;

    public Path getImageStoragePath() {
        Path path = Paths.get(System.getProperty("user.dir"), imageStoragePath);

        try{
            Files.createDirectories(path);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to create image storage directory", exception);
        }

        return path;
    }

}
