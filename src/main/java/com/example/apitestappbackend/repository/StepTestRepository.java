package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.StepTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepTestRepository extends JpaRepository<StepTest, Integer> {
}
