package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    public UserResponse register(UserRequest request){
        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already exist");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        userRepository.save(user);
        return UserResponse.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
                .build();
    }

    public UserResponse getUser(User user){
        authService.getToken();
        return UserResponse.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
                .build();
    }
}
