package com.example.burnttoast.mapper;

import com.example.burnttoast.dto.RecipeDTO;
import com.example.burnttoast.model.Recipe;

public class RecipeMapper {
    public static RecipeDTO toDTO(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        recipeDTO.setTitle(recipe.getTitle());
        recipeDTO.setUrl(recipe.getUrl());
        recipeDTO.setNotes(recipe.getNotes());
        recipeDTO.setCategoryId(recipe.getCategory().getId());
        recipeDTO.setStatus(recipe.getStatus());
        recipeDTO.setCookingTime(recipe.getCookingTime());
        recipeDTO.setRating(recipe.getRating());
        recipeDTO.setTags(recipe.getTags().stream().map(TagMapper::toDTO).toList());
        return recipeDTO;
    }

    public static Recipe toEntity(Recipe recipe, RecipeDTO recipeDTO)
    {
        recipe.setUrl(recipeDTO.getUrl());
        recipe.setNotes(recipeDTO.getNotes());
        recipe.setTitle(recipeDTO.getTitle());
        recipe.setStatus(recipeDTO.getStatus());
        recipe.setRating(recipeDTO.getRating());
        recipe.setCookingTime(recipeDTO.getCookingTime());
        return  recipe;
    }
}
