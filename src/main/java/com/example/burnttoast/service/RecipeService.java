package com.example.burnttoast.service;

import com.example.burnttoast.dto.RecipeDTO;
import com.example.burnttoast.exception.ResourceNotFoundException;
import com.example.burnttoast.mapper.RecipeMapper;
import com.example.burnttoast.model.Category;
import com.example.burnttoast.model.Recipe;
import com.example.burnttoast.model.RecipeStatus;
import com.example.burnttoast.repository.CategoryRepository;
import com.example.burnttoast.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.burnttoast.mapper.RecipeMapper.toDTO;
import static com.example.burnttoast.mapper.RecipeMapper.toEntity;

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
                .map(RecipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RecipeDTO create(Long categoryId, RecipeDTO recipeDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
        Recipe recipe = toEntity(new Recipe(), recipeDTO);
        recipe.setCategory(category);
        Recipe createdRecipe = recipeRepository.save(recipe);
        return toDTO(createdRecipe);
    }

    public RecipeDTO update(Long recipeId, RecipeDTO recipeDTO) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found."));
        toEntity(recipe, recipeDTO);
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return toDTO(updatedRecipe);
    }

    public void delete(Long recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    public List<RecipeDTO> search(String query, Long categoryId, RecipeStatus status, Long tagId) {
        return recipeRepository.search(query, categoryId, status, tagId)
                .stream()
                .map(RecipeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
