package com.example.burnttoast.controller;

import com.example.burnttoast.config.JwtUtil;
import com.example.burnttoast.dto.RefreshTokenResponseDTO;
import com.example.burnttoast.exception.InvalidCredentialsException;
import com.example.burnttoast.service.RefreshTokenService;
import org.springframework.web.bind.annotation.*;

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
    public RefreshTokenResponseDTO create(@RequestBody String token){
        var isTokenValid = refreshTokenService.checkIsRefreshTokenValid(token);
        if (isTokenValid){
           var username = refreshTokenService.getUsernameFromRefreshToken(token);
            var response = new RefreshTokenResponseDTO();
            response.setToken(jwtUtil.generateToken(username));
            return response;
        }
        else {
            throw new InvalidCredentialsException("Login expired.");
        }
    }

    @DeleteMapping("/auth/logout")
    public void delete(@RequestBody String refreshToken){
        refreshTokenService.revokeRefreshToken(refreshToken);
    }
}
