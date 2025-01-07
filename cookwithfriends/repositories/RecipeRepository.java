package com.bee.cookwithfriends.repositories;

import com.bee.cookwithfriends.entity.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

    Optional<Recipe> findById(int id);
    List<Recipe> findByTitleContainingIgnoreCase(String username);

    @Query(value = "SELECT * FROM recipe ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Recipe> getRandomRecipe();


    @Query(value = "SELECT * FROM recipe ORDER BY RAND() LIMIT :count", nativeQuery = true)
    Optional<List<Recipe>> getRandomStackOfRecipes(@Param("count") int count);
}
