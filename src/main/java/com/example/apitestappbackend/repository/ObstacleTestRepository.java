package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.ObstacleTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObstacleTestRepository extends JpaRepository<ObstacleTest, Integer> {
}
