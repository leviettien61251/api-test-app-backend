package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.StepTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepTestRepository extends JpaRepository<StepTest, Integer> {
    List<StepTest> findByMapTest_Id(Integer mapId);
}
