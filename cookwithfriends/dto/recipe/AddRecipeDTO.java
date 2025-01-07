package com.bee.cookwithfriends.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddRecipeDTO {

    private String title;
    private String description;
    private List<InstructionDTO> instructions;
    private List<RecipeIngredientDTO> recipeIngredients;
    private int cookingTime;
    private int preparationTime;
    private int servings;

}
