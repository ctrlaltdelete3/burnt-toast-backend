package com.example.burnttoast.controller;

import com.example.burnttoast.dto.RecipeDTO;
import com.example.burnttoast.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService)
    {
        this.recipeService = recipeService;
    }

    @GetMapping("/categories/{categoryId}/recipes")
    public List<RecipeDTO> getAll(@PathVariable Long categoryId) {
        return recipeService.getAllByCategory(categoryId);
    }

    @PostMapping("/categories/{categoryId}/recipes")
    public RecipeDTO create(@PathVariable Long categoryId,@RequestBody RecipeDTO recipeDTO) {
        return recipeService.create(categoryId, recipeDTO);
    }

    @PutMapping("/recipes/{id}")
    public RecipeDTO update(@PathVariable Long id, @RequestBody RecipeDTO recipeDTO)
    {
        return  recipeService.update(id, recipeDTO);
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        recipeService.delete(id);
        return  ResponseEntity.noContent().build();
    }
}
