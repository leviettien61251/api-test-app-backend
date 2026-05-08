package com.example.apitestappbackend.models.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "error_events", indexes = {
        @Index(name = "idx_user_occurred", columnList = "user_id, occurred_at"),
        @Index(name = "idx_error_occurred", columnList = "error_type, occurred_at"),
        @Index(name = "idx_test_run_id", columnList = "test_run_id"),
        @Index(name = "idx_collection_id", columnList = "collection_id")
})
public class ErrorEvent {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @ManyToOne
    @JoinColumn(name = "test_run_id")
    private TestRun testRun;

    @ManyToOne
    @JoinColumn(name = "client_session_id")
    private ClientSession clientSession;

    @Column(name = "error_type", length = 100)
    private String errorType; // 'CONNECTION_ERROR', 'TIMEOUT', 'ASSERTION_FAILURE', 'SCRIPT_ERROR', 'PARSE_ERROR'

    @Column(length = 20)
    private String severity = "ERROR"; // 'WARNING', 'ERROR', 'CRITICAL'

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "error_stack", columnDefinition = "TEXT")
    private String errorStack;

    @Column(name = "request_url", columnDefinition = "TEXT")
    private String requestUrl;

    @Column(name = "request_method", length = 20)
    private String requestMethod;

    @CreationTimestamp
    @Column(name = "occurred_at", updatable = false)
    private LocalDateTime occurredAt;
}
