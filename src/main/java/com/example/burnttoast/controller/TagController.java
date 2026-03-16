package com.example.burnttoast.controller;

import com.example.burnttoast.dto.RecipeDTO;
import com.example.burnttoast.dto.TagDTO;
import com.example.burnttoast.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public List<TagDTO> getAll() {
        return tagService.getAll();
    }

    @PostMapping("/tags")
    public TagDTO create(@RequestBody TagDTO tagDTO){
        return  tagService.create(tagDTO);
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recipes/{recipeId}/tags/{tagId}")
    public ResponseEntity<Void> addTagToRecipe(@PathVariable Long recipeId, @PathVariable Long tagId ){
        tagService.addTagToRecipe(recipeId, tagId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/recipes/{recipeId}/tags/{tagId}")
    public ResponseEntity<Void> removeTagFromRecipe(@PathVariable Long recipeId, @PathVariable Long tagId ){
        tagService.removeTagFromRecipe(recipeId, tagId);
        return ResponseEntity.noContent().build();
    }
}
