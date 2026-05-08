package com.example.apitestappbackend.models.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "test_runs", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_test_suite_id", columnList = "test_suite_id"),
        @Index(name = "idx_collection_id", columnList = "collection_id"),
        @Index(name = "idx_client_session_id", columnList = "client_session_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
public class TestRun {
    @Id
    @UuidGenerator
    private String id;

    @Column(length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "client_session_id")
    private ClientSession clientSession;

    @ManyToOne
    @JoinColumn(name = "test_suite_id")
    private TestSuite testSuite;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @Column(name = "run_type", length = 20)
    private String runType = "TEST_SUITE"; // 'TEST_SUITE', 'COLLECTION', 'SINGLE_REQUEST', 'SINGLE_SCENARIO'

    @Column(name = "run_mode", length = 20)
    private String runMode = "ALL"; // 'ALL', 'SINGLE'

    @Column(name = "executed_by", length = 255)
    private String executedBy;

    @Column(name = "client_hostname", length = 255)
    private String clientHostname;

    @Column(name = "client_os", length = 100)
    private String clientOs;

    @Column(name = "api_base_url", columnDefinition = "TEXT")
    private String apiBaseUrl;

    @Column(length = 50)
    private String status = "PENDING"; // 'PENDING', 'RUNNING', 'COMPLETED', 'STOPPED', 'FAILED'

    @Column(name = "stop_on_failure")
    private Boolean stopOnFailure = false;

    @Column(name = "alert_mode", length = 20)
    private String alertMode = "REPORT_ALL"; // 'STOP_ON_FAILURE', 'REPORT_ALL'

    @Column(name = "total_tests")
    private Integer totalTests = 0;

    @Column(name = "passed_tests")
    private Integer passedTests = 0;

    @Column(name = "failed_tests")
    private Integer failedTests = 0;

    @Column(name = "error_tests")
    private Integer errorTests = 0;

    @Column(name = "skipped_tests")
    private Integer skippedTests = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "result_summary", columnDefinition = "JSON")
    private Object resultSummary;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
