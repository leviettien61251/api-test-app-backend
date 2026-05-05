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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_AVATAR_USER_TEST"))
    private UserTest user_id;

    @Column(name = "retrieved_full_name", length = 255)
    private String retrievedFullName;

    @Column(name = "retrieved_phone", length = 20)
    private String retrievedPhone;

    @Column(name = "retrieved_avatar", columnDefinition = "TEXT")
    private String retrievedAvatar;

    @Column(name = "retrieved_address", columnDefinition = "TEXT")
    private String retrievedAddress;

    @Column(length = 50, columnDefinition = "varchar(50)")
    private String status = "success";

    @Column(name = "code",  columnDefinition = "TEXT")
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
