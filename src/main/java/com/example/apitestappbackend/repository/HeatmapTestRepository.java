package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.HeatmapTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeatmapTestRepository extends JpaRepository<HeatmapTest, Integer> {
}
