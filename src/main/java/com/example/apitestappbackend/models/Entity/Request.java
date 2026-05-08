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
@Table(name = "requests")
public class Request {
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

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(length = 20)
    private String method; // GET, POST, PUT, DELETE, PATCH, etc.

    @Column(columnDefinition = "TEXT")
    private String url;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "path_variables", columnDefinition = "JSON")
    private Object pathVariables;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "query_params", columnDefinition = "JSON")
    private Object queryParams;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Object headers;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Object auth;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Object body;

    @Column(name = "body_mode", length = 50)
    private String bodyMode; // none, json, xml, formdata, urlencoded, raw, binary

    @Column(name = "pre_request_script", columnDefinition = "TEXT")
    private String preRequestScript;

    @Column(name = "test_script", columnDefinition = "TEXT")
    private String testScript;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Object settings;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}