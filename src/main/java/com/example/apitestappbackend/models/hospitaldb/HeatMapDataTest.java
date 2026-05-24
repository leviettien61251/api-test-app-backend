package com.example.apitestappbackend.models.hospitaldb;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "heatmap_data_tests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatMapDataTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "route_id", nullable = false, referencedColumnName = "route_id")
    private RouteTest routeId;

    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;

    @Column(name = "density_value")
    private Double densityValue;

    @Column(name = "radius")
    private Double radius;

    @Column(name = "status_message", columnDefinition = "TEXT")
    private String statusMessage;

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
