package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.MapTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapTestRepository extends JpaRepository<MapTest, Integer> {
    boolean existsByBuildingCode(String buildingCode);

    List<MapTest> findByBuildingCode(String buildingCode);

    List<MapTest> findByBuildingCodeContainingOrBuildingCodeIsNull(String buildingCode);

    List<MapTest> findMapTestsByBuildingCode(String buildingCode);
}
