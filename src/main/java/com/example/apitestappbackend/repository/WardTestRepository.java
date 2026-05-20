package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.WardTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardTestRepository extends JpaRepository<WardTest, Integer> {
    List<WardTest> findByNameContainingIgnoreCase(String keyword);
}
