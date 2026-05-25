package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.RouteDensityTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteDensityTestRepository extends JpaRepository<RouteDensityTest, Integer> {
    @Query("""
            SELECT rd.type
            FROM RouteDensityTest rd
            WHERE rd.routeId.routeId = :routeId
            """)
    Optional<Integer> findCurrentPeopleByRouteId(@Param("routeId") String routeId);
}
