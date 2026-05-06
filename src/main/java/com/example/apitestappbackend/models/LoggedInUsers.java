package com.example.apitestappbackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;

@Entity
@Table(name = "logged_in_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedInUsers {
    @Id
    @UuidGenerator
    private String id;
    @Column(nullable = false, length = 20)
    private String phoneNumber;
    @Column(nullable = false, length = 100)
    private String password;
    @CreationTimestamp
    private Timestamp loginTimestamp;
    @Column(length = 50, columnDefinition = "varchar(50) default 'success'")
    private String loginStatus;
    @Column(columnDefinition = "TEXT")
    private String token;
    @Column(columnDefinition = "TEXT")
    private String refreshToken;
    private Timestamp tokenExpiresAt;
    @Column(columnDefinition = "TEXT")
    private String code;
    @Column(columnDefinition = "TEXT")
    private String message;
    @Column(columnDefinition = "boolean default false")
    private Boolean usedInTest;
    @CreationTimestamp
    private Timestamp createdAt;
}
