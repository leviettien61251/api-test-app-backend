package com.example.apitestappbackend.models.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "test_assertions")
public class TestAssertion {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "test_execution_id")
    private TestExecution testExecution;

    @Column(name = "assertion_name", length = 255)
    private String assertionName;

    @Column(name = "assertion_type", length = 50)
    private String assertionType; // 'STATUS_CODE', 'RESPONSE_BODY', 'HEADER', 'RESPONSE_TIME', 'JSON_PATH', 'SCHEMA'

    @Column(name = "target_path", columnDefinition = "TEXT")
    private String targetPath;

    @Column(name = "expected_value", columnDefinition = "TEXT")
    private String expectedValue;

    @Column(name = "actual_value", columnDefinition = "TEXT")
    private String actualValue;

    @Column(name = "comparison_operator", length = 20)
    private String comparisonOperator; // 'EQUALS', 'CONTAINS', 'MATCHES', 'GT', 'LT', 'EXISTS'

    private Boolean passed;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "executed_at", updatable = false)
    private LocalDateTime executedAt;
}