package com.example.burnttoast.dto;

import lombok.Data;

//Note: this is internal transfer object (not DTO)
@Data
public class LoginResult {
    private String accessToken;
    private String refreshToken;
}
