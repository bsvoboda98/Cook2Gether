package com.bee.cookwithfriends.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSmallDTO {

    private int id;
    private String title;
    private String description;
    private int cookingTime;
    private int rating;

}
