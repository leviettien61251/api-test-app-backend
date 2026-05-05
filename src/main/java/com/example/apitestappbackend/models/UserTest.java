package com.example.apitestappbackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "users_test")
public class UserTest {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "phone_number", length = 20, unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "fullname", length = 255)
    private String fullname;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "token_expires_at")
    private Timestamp tokenExpiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "last_login_time")
    private Timestamp lastLoginTime;
}

