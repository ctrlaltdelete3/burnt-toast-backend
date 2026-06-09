package com.example.burnttoast.controller;

import com.example.burnttoast.dto.AuthRequestDTO;
import com.example.burnttoast.dto.AuthResponseDTO;
import com.example.burnttoast.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AuthResponseDTO register(@RequestBody AuthRequestDTO authRequestDTO) {
        return userService.register(authRequestDTO);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response) {

        var loginResult = userService.login(authRequestDTO);

        Cookie cookie = new Cookie("refreshToken", loginResult.getRefreshToken());

        cookie.setHttpOnly(true);
        cookie.setSecure(false); //TODO: change when you enable HTTPS (currently only HTTP)
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30); //30 days - in seconds

        response.addCookie(cookie);

        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken(loginResult.getAccessToken());
        return authResponseDTO;
    }
}
