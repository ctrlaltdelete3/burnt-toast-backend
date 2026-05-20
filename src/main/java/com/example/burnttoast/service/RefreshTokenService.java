package com.example.burnttoast.service;

import com.example.burnttoast.config.JwtUtil;
import com.example.burnttoast.exception.InvalidCredentialsException;
import com.example.burnttoast.exception.ResourceNotFoundException;
import com.example.burnttoast.model.RefreshToken;
import com.example.burnttoast.model.User;
import com.example.burnttoast.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

import java.time.LocalDateTime;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
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

    public String getUserIfRefreshTokenIsValid(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found."));
        var isTokenExpired = refreshToken.getExpiresAt().isBefore(LocalDateTime.now());

        if (refreshToken.isRevoked() || isTokenExpired) {
            throw new InvalidCredentialsException("Refresh token is expired.");
        }

        return refreshToken.getUser().getUsername();
    }

    public String generateAccessToken(String username) {
        return jwtUtil.generateToken(username);
    }
}
