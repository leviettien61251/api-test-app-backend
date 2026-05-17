package com.example.apitestappbackend.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "node_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "map_id", nullable = false)
    private MapTest mapTest;

    @Column(name = "x_coordinate", nullable = false)
    private Double XCoordinate;

    @Column(name = "y_coordinate", nullable = false)
    private Double YCoordinate;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "is_passable", nullable = false)
    private Boolean isPassable = true;

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
