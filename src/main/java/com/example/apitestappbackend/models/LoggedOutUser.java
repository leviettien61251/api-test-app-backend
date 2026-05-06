package com.example.apitestappbackend.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "logout")
public class LoggedOutUser {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    @Column(name = "invalidated_token", columnDefinition = "TEXT")
    private String invalidatedToken;

    @Column(length = 50, columnDefinition = "varchar(50)")
    private String status = "success";

    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "time_stamp", length = 50)
    private String timeStamp;

    @Column(name = "used_in_test")
    private Boolean usedInTest = false;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
}

