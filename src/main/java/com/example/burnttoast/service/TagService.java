package com.example.burnttoast.service;

import com.example.burnttoast.dto.TagDTO;
import com.example.burnttoast.exception.ResourceNotFoundException;
import com.example.burnttoast.mapper.TagMapper;
import com.example.burnttoast.model.Recipe;
import com.example.burnttoast.model.Tag;
import com.example.burnttoast.repository.RecipeRepository;
import com.example.burnttoast.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.burnttoast.mapper.TagMapper.toDTO;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final RecipeRepository recipeRepository;

    public TagService(TagRepository tagRepository, RecipeRepository recipeRepository) {
        this.tagRepository = tagRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<TagDTO> getAll() {
        return tagRepository.findAll()
                .stream()
                .map(TagMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TagDTO create(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        Tag createdTag = tagRepository.save(tag);
        return toDTO(createdTag);
    }

    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

    public void addTagToRecipe(Long recipeId, Long tagId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found."));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found."));
        if (!recipe.getTags().contains(tag)){
            recipe.getTags().add(tag);
            recipeRepository.save(recipe);
        }
    }

    public void removeTagFromRecipe(Long recipeId, Long tagId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found."));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found."));
        if(recipe.getTags().remove(tag)){
            recipeRepository.save(recipe);
        }
    }
}
