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
@Table(name = "report_instances", indexes = {
        @Index(name = "idx_template_id", columnList = "template_id"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_test_run_id", columnList = "test_run_id"),
        @Index(name = "idx_generated_at", columnList = "generated_at")
})
public class ReportInstance {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private ReportTemplate template;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_run_id")
    private TestRun testRun;

    @Column(name = "file_name", length = 500)
    private String fileName;

    @Column(name = "file_path", columnDefinition = "TEXT")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_format", length = 20)
    private String fileFormat;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "chart_data", columnDefinition = "JSON")
    private Object chartData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Object summary;

    @CreationTimestamp
    @Column(name = "generated_at", updatable = false)
    private LocalDateTime generatedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
