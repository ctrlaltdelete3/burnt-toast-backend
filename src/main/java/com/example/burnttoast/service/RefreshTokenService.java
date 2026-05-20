package com.example.burnttoast.service;

import com.example.burnttoast.exception.ResourceNotFoundException;
import com.example.burnttoast.model.RefreshToken;
import com.example.burnttoast.model.User;
import com.example.burnttoast.repository.RefreshTokenRepository;
import com.example.burnttoast.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

import java.time.LocalDateTime;

@Service
public class RefreshTokenService{
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRevoked(false);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(30));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public void revokeRefreshToken(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Refresh token not found."));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    public boolean checkIsRefreshTokenValid(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Refresh token not found."));
        var isTokenExpired = refreshToken.getExpiresAt().isBefore(LocalDateTime.now());
        return !refreshToken.isRevoked() && !isTokenExpired;
    }

    public String getUsernameFromRefreshToken(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found."));
        return refreshToken.getUser().getUsername();
    }
}
