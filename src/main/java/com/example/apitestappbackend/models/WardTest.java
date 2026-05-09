package com.example.apitestappbackend.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "ward_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WardTest {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "map_node_id", nullable = false)
    private String mapNodeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private String status = "open";
}
