package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.WebResponse;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;

@RestController
@RequestMapping("/api")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public WebResponse<TokenResponse> login(@RequestBody AuthRequest request){
        TokenResponse response = authService.login(request);
        return WebResponse.<TokenResponse>builder().data(response).build();
    }

    @DeleteMapping("/logout")
    public WebResponse<TokenResponse> logout(User user){
        TokenResponse response = authService.logout(user);
        return WebResponse.<TokenResponse>builder().data(response).build();
    }

    @GetMapping("/getTokenFromRedis")
    public WebResponse<TokenResponse> getTokenFromRedis() {
        TokenResponse tokenResponse = authService.getToken();
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }
}
