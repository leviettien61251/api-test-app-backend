package com.example.apitestappbackend.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "node_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeTest {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "map_id", nullable = false)
    private MapTest mapTest;

    @Column(name = "x_coordinate", nullable = false)
    private Double xCoordinate;

    @Column(name = "y_coordinate", nullable = false)
    private Double yCoordinate;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "is_passable", nullable = false)
    private Boolean isPassable = true;
}
