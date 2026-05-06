package com.example.apitestappbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "set_user_info")
@NoArgsConstructor
@AllArgsConstructor
public class SetUserInfo {

    @Id
    @UuidGenerator
    private String  id;

    @Column(name = "phone_number", length = 20, unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "new_full_name", length = 255)
    private String newFullName;

    @Column(name = "new_address", columnDefinition = "TEXT")
    private String newAddress;

    @Column(length = 50, columnDefinition = "varchar(50)")
    private String status = "success";

    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "time_stamp", length = 50)
    private Timestamp timeStamp;

    @Column(name = "used_in_test")
    private Boolean usedInTest = false;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
}
