package com.example.demo.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;
    
    private static final String KEY = "TOKEN:";

    public TokenResponse login(AuthRequest request){
        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized"));

        user.setToken(UUID.randomUUID().toString());
        user.setExpiredToken(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30));
        userRepository.save(user);
        putToken(user.getToken(), user.getExpiredToken());
        return TokenResponse.builder()
                .token(user.getToken())
                .expiredToken(user.getExpiredToken())
                .build();
    }

    public TokenResponse logout(User user){
        user.setToken(null);
        user.setExpiredToken(null);
        userRepository.save(user);
        return TokenResponse.builder()
                .token(user.getToken())
                .expiredToken(user.getExpiredToken())
                .build();
    }

    //REDIS
    @SuppressWarnings("unchecked")
    public void putToken(String token, long expiredToken) {
        redisTemplate.opsForHash().put(KEY, "token", token);
        redisTemplate.opsForHash().put(KEY, "expiredToken", expiredToken);
    }

    @SuppressWarnings("unchecked")
    public TokenResponse getToken() {
        String token = (String) redisTemplate.opsForHash().get(KEY, "token");
        Long expiredToken = (Long) redisTemplate.opsForHash().get(KEY, "expiredToken");

        if (token != null && expiredToken != null) {
            return TokenResponse.builder()
                    .token(token)
                    .expiredToken(expiredToken)
                    .build();
        } else {
            return null;
        }
    }
}
