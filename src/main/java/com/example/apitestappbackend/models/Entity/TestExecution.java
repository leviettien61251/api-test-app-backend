package com.example.apitestappbackend.models.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "test_executions")
public class TestExecution {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "test_run_id")
    private TestRun testRun;

    @ManyToOne
    @JoinColumn(name = "test_suite_item_id")
    private TestSuiteItem testSuiteItem;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private TestScenario scenario;

    @Column(name = "execution_type", length = 20)
    private String executionType; // 'REQUEST', 'SCENARIO'

    @Column(name = "response_status")
    private Integer responseStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "response_headers", columnDefinition = "JSON")
    private Object responseHeaders;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "response_size")
    private Long responseSize;

    @Column(name = "response_time")
    private Integer responseTime;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "test_results", columnDefinition = "JSON")
    private Object testResults;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(length = 50)
    private String status = "PENDING"; // 'PENDING', 'RUNNING', 'PASSED', 'FAILED', 'ERROR', 'SKIPPED'

    @Column(name = "attempt_number")
    private Integer attemptNumber = 1;

    @Column(name = "retry_reason", columnDefinition = "TEXT")
    private String retryReason;

    @Column(name = "executed_at")
    private LocalDateTime executedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
