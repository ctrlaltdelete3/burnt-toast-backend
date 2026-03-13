package com.example.burnttoast.service;

import com.example.burnttoast.dto.RecipeDTO;
import com.example.burnttoast.model.Category;
import com.example.burnttoast.model.Recipe;
import com.example.burnttoast.repository.CategoryRepository;
import com.example.burnttoast.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    public RecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<RecipeDTO> getAllByCategory(Long categoryId) {
        return recipeRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RecipeDTO create(Long categoryId, RecipeDTO recipeDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found."));
        Recipe recipe = new Recipe();
        recipe.setUrl(recipeDTO.getUrl());
        recipe.setTitle(recipeDTO.getTitle());
        recipe.setNotes(recipeDTO.getNotes());
        recipe.setCategory(category);
        Recipe createdRecipe = recipeRepository.save(recipe);
        return toDTO(createdRecipe);
    }

    public RecipeDTO update(Long recipeId, RecipeDTO recipeDTO) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found."));
        recipe.setUrl(recipeDTO.getUrl());
        recipe.setNotes(recipeDTO.getNotes());
        recipe.setTitle(recipeDTO.getTitle());
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return toDTO(updatedRecipe);
    }

    public void delete(Long recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    private RecipeDTO toDTO(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        recipeDTO.setTitle(recipe.getTitle());
        recipeDTO.setUrl(recipe.getUrl());
        recipeDTO.setNotes(recipe.getNotes());
        recipeDTO.setCategoryId(recipe.getCategory().getId());
        return recipeDTO;
    }
}
