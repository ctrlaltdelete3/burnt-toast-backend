package com.example.burnttoast.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime expiresAt;
}
