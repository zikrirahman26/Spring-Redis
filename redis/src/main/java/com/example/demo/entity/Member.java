package com.example.demo.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "members_redis")
public class Member {
    
    @Id
    private String id;

    private String name;

    private String job;

    private String tittle;

    @PrePersist
    private void PrePersist(){
        if (id == null || id.isEmpty()) {
            id = generateUUID();
        }
    }

    private String generateUUID(){
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
