package com.example.burnttoast.controller;

import com.example.burnttoast.dto.AuthRequestDTO;
import com.example.burnttoast.dto.AuthResponseDTO;
import com.example.burnttoast.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public AuthResponseDTO register(@RequestBody AuthRequestDTO authRequestDTO){
        return  userService.register(authRequestDTO);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO authRequestDTO){
        return  userService.login(authRequestDTO);
    }
}
