package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.PathTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PathTestRepository extends JpaRepository<PathTest, Integer> {
}
