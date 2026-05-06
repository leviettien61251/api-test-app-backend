package com.example.apitestappbackend.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserInfo {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

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
