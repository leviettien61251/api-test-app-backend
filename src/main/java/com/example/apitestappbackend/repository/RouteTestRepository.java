package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.RouteTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteTestRepository extends JpaRepository<RouteTest, Integer> {
    Optional<RouteTest> findByRouteId(String routeId);
}
