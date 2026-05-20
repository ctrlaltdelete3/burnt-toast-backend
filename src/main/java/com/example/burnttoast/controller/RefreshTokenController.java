package com.example.burnttoast.controller;

import com.example.burnttoast.config.JwtUtil;
import com.example.burnttoast.dto.RefreshTokenResponseDTO;
import com.example.burnttoast.exception.InvalidCredentialsException;
import com.example.burnttoast.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api")
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    public RefreshTokenController(RefreshTokenService refreshTokenService, JwtUtil jwtUtil) {
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/auth/refresh")
    public RefreshTokenResponseDTO create(HttpServletRequest request) {
        String refreshToken = getRefreshToken(request);
        var username = refreshTokenService.getUserIfRefreshTokenIsValid(refreshToken);
        var response = new RefreshTokenResponseDTO();
        response.setToken(jwtUtil.generateToken(username));
        return response;
    }

    @PostMapping("/auth/logout")
    public void delete(HttpServletRequest request) {
        String refreshToken = getRefreshToken(request);
        refreshTokenService.revokeRefreshToken(refreshToken);
    }

    private String getRefreshToken(HttpServletRequest request) {
        var cookies = request.getCookies();
        if (cookies == null) {
            throw new InvalidCredentialsException("Refresh token not found.");
        }

        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .orElseThrow(() -> new InvalidCredentialsException("Refresh token not found."))
                .getValue();
    }
}
