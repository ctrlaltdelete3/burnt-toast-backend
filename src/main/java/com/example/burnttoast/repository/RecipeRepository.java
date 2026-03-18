package com.example.burnttoast.repository;

import com.example.burnttoast.model.Recipe;
import com.example.burnttoast.model.RecipeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByCategoryId(Long categoryId);

    @Query("SELECT r FROM Recipe r " +
            "LEFT JOIN r.tags t " +
            "WHERE (:query IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:categoryId IS NULL OR r.category.id = :categoryId) " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND (:tagId IS NULL OR t.id = :tagId)")
    List<Recipe> search(@Param("query") String query,
                        @Param("categoryId") Long categoryId,
                        @Param("status") RecipeStatus status,
                        @Param("tagId") Long tagId);
}
