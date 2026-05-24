package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.BottlenecksDataTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BottlenecksDataTestRepository extends JpaRepository<BottlenecksDataTest, Integer> {
    List<BottlenecksDataTest> findByRouteId_RouteIdAndOccupancyRateGreaterThan(String routeId, Double occupancyRate);
}
