package com.example.apitestappbackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;

@Entity
@Table(name = "password_changed")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChanged {
    @Id
    @UuidGenerator
    private String id;
    @Column(length = 20, unique = true)
    private String phoneNumber;
    @Column(nullable = false, length = 100)
    private String oldPassword;
    @Column(nullable = false, length = 100)
    private String newPassword;
    @Column(length = 20)
    private String oldPasswordStatus;     // "PROVIDED" | "NULL" | "EMPTY"
    @Column(length = 20)
    private String newPasswordStatus;     // "PROVIDED" | "NULL" | "TOO_SHORT" | "SAME_AS_OLD"
    @CreationTimestamp
    private Timestamp passwordChangedTimestamp;
    @Column(length = 50, columnDefinition = "varchar(50)")
    @Builder.Default
    private String status = "success";
    @Column(columnDefinition = "TEXT")
    private String code;
    @Column(columnDefinition = "TEXT")
    private String message;
    @Column(name = "used_in_test")
    @Builder.Default
    private Boolean usedInTest = false;
}
