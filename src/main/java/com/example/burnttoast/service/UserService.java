package com.example.burnttoast.service;

import com.example.burnttoast.config.JwtUtil;
import com.example.burnttoast.dto.AuthRequestDTO;
import com.example.burnttoast.dto.AuthResponseDTO;
import com.example.burnttoast.exception.InvalidCredentialsException;
import com.example.burnttoast.exception.ResourceNotFoundException;
import com.example.burnttoast.exception.UserAlreadyExistsException;
import com.example.burnttoast.model.Role;
import com.example.burnttoast.model.User;
import com.example.burnttoast.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO register(AuthRequestDTO authRequestDTO) {
        User user = userRepository.findByUsername(authRequestDTO.getUsername());
        if (user != null) {
            //todo: maybe redirect to login somehow
            throw new UserAlreadyExistsException("User already registered.");
        }
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(authRequestDTO.getPassword()));
        newUser.setEmail(authRequestDTO.getEmail());
        newUser.setUsername(authRequestDTO.getUsername());
        newUser.setRole(Role.USER);
        userRepository.save(newUser);
        String token = jwtUtil.generateToken(authRequestDTO.getUsername());
        return generateAuthResponseDTO(token);
    }

    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) {
        User user = userRepository.findByUsername(authRequestDTO.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }
        if(!passwordEncoder.matches(authRequestDTO.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid password.");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return generateAuthResponseDTO(token);
    }

    private AuthResponseDTO generateAuthResponseDTO(String token){
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken(token);
        return authResponseDTO;
    }
}
