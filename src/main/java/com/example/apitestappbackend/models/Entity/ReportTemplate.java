package com.example.apitestappbackend.models.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.hibernate.annotations.JdbcTypeCode;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "report_templates")
public class ReportTemplate {
    @Id
    @UuidGenerator
    private String id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "report_type", length = 50)
    private String reportType; // 'TEST_RUN', 'COLLECTION', 'TREND', 'PERFORMANCE', 'ERROR_ANALYSIS'

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Object filters;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Object schedule;

    @Column(length = 20)
    private String format = "PDF"; // 'PDF', 'HTML', 'JSON', 'CSV', 'EXCEL'

    @Column(name = "is_scheduled")
    private Boolean isScheduled = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
