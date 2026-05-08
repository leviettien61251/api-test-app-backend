package com.example.apitestappbackend.models.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "test_scenarios", indexes = {
        @Index(name = "idx_collection_id", columnList = "collection_id"),
        @Index(name = "idx_class_method", columnList = "class_name, method_name", unique = true)
})
public class TestScenario {
    @Id
    @UuidGenerator
    private String id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @Column(name = "class_name", length = 500, nullable = false)
    private String className;

    @Column(name = "method_name", length = 255, nullable = false)
    private String methodName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Object parameters;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Object tags;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
