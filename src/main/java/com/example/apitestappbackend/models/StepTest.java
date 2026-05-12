package com.example.apitestappbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.relational.core.sql.In;

@Entity
@Table(name = "step_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "map_id", nullable = false)
    private MapTest mapTest;

    @Column(name = "start_node_id", length = 50, nullable = false)
    private String startNodeId;

    @Column(name = "end_node_id", length = 50, nullable = false)
    private String endNodeId;

    @Column(name = "distance", nullable = false)
    private Double distance;

    @Column(name = "direction", length = 50)
    private String direction;

    @Column(name = "instruction")
    private String instruction;

}
