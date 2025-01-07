package com.bee.cookwithfriends.repositories;

import com.bee.cookwithfriends.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient, Integer> {

    Optional<Ingredient> findByName(String name);

    //Optional<Ingredient> findById(int id);


}
