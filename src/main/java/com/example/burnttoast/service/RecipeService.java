package com.example.burnttoast.service;

import com.example.burnttoast.dto.RecipeDTO;
import com.example.burnttoast.exception.ResourceNotFoundException;
import com.example.burnttoast.mapper.RecipeMapper;
import com.example.burnttoast.model.Category;
import com.example.burnttoast.model.Recipe;
import com.example.burnttoast.model.RecipeStatus;
import com.example.burnttoast.model.User;
import com.example.burnttoast.repository.CategoryRepository;
import com.example.burnttoast.repository.RecipeRepository;
import com.example.burnttoast.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.burnttoast.mapper.RecipeMapper.toDTO;
import static com.example.burnttoast.mapper.RecipeMapper.toEntity;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<RecipeDTO> getAllByCategoryForCurrentUser(Long categoryId) {
        var user = getCurrentlyLoggedUser();
        return recipeRepository.findByCategoryIdAndUserId(categoryId, user.getId())
                .stream()
                .map(RecipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RecipeDTO create(Long categoryId, RecipeDTO recipeDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
        Recipe recipe = toEntity(new Recipe(), recipeDTO);
        recipe.setCategory(category);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setThumbnailUrl(fetchThumbnail(recipeDTO.getUrl()));
        recipe.setUser(getCurrentlyLoggedUser());
        Recipe createdRecipe = recipeRepository.save(recipe);
        return toDTO(createdRecipe);
    }

    public RecipeDTO update(Long recipeId, RecipeDTO recipeDTO) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found."));
        toEntity(recipe, recipeDTO);
        recipe.setThumbnailUrl(fetchThumbnail(recipeDTO.getUrl()));
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return toDTO(updatedRecipe);
    }

    public void delete(Long recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    //TODO: NOTE - currently, search by tags is not implemented in frontend!
    public List<RecipeDTO> search(String query, Long categoryId, RecipeStatus status, Long tagId) {
        return recipeRepository.search(query, categoryId, status, tagId)
                .stream()
                .map(RecipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    private String fetchThumbnail(String url){
        if (url == null || url.isBlank())
            return null;
        try {
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get()
                    .select("meta[property=og:image]")
                    .attr("content");

        }catch(Exception e){
            return null;
        }
    }

    private User getCurrentlyLoggedUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }
}
