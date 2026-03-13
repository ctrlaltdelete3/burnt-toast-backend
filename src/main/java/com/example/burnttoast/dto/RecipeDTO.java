package com.example.burnttoast.dto;

import lombok.Data;

@Data
public class RecipeDTO {
    private Long id;
    private String title;
    private String url;
    private String notes;
    private Long categoryId ;
}
