package com.example.burnttoast.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    public String username;
    public String email;
    public String password;
}
