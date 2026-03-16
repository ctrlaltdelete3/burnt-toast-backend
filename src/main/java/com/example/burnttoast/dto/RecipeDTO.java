package com.example.burnttoast.dto;

import com.example.burnttoast.model.RecipeStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecipeDTO {
    private Long id;
    private String title;
    private String url;
    private String notes;
    private Long categoryId ;
    private RecipeStatus status;
    private Integer cookingTime;
    private Integer rating; // can be 1-5
    private List<TagDTO> tags = new ArrayList<>();
}
