package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.HeatMapDataTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeatMapDataTestRepository extends JpaRepository<HeatMapDataTest, Integer> {
    List<HeatMapDataTest> findByRouteId_RouteId(String routeId);
}
