package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_token_auth_redis")
public class User {
    
    @Id
    private String username;

    private String password;
    
    private String name;

    private String token;

    private Long expiredToken;
}
