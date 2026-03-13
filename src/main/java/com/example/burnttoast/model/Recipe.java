package com.example.burnttoast.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recipes")
@Data
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String url;
    private String notes;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
