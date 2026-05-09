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
@Table(name = "route_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteTest {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "route_name", length = 100, nullable = false)
    private String routeName;
}
