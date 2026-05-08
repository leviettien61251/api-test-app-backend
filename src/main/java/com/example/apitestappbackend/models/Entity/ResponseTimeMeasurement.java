package com.example.apitestappbackend.models.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "response_time_measurements")
public class ResponseTimeMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @Column(name = "response_time")
    private Integer responseTime;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "response_size")
    private Long responseSize;

    private Boolean success;

    @Column(length = 100)
    private String region;

    @CreationTimestamp
    @Column(name = "measured_at", updatable = false)
    private LocalDateTime measuredAt;
}