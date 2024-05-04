package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.WebResponse;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public WebResponse<UserResponse> register(@RequestBody UserRequest request){
        UserResponse response = userService.register(request);
        return WebResponse.<UserResponse>builder().data(response).build();
    }

    @GetMapping("/user")
    public WebResponse<UserResponse> getUser(User user){
        UserResponse response = userService.getUser(user);
        authService.getToken();
        return WebResponse.<UserResponse>builder().data(response).build();
    }
}
