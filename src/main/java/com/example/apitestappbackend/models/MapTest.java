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
@Table(name = "maps_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapTest {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "building_code", length = 50, nullable = false)
    private String buildingCode;

    @Column(name = "building_name", length = 100, nullable = false)
    private String buildingName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "scale_x", nullable = false)
    private Double scaleX;

    @Column(name = "scale_y", nullable = false)
    private Double scaleY;

    @Column(length = 50, columnDefinition = "varchar(50)")
    private String status = "success";

    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    @Column(name = "time_stamp", length = 50)
    private Timestamp timeStamp;

    @Column(name = "used_in_test")
    private Boolean usedInTest = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
}
