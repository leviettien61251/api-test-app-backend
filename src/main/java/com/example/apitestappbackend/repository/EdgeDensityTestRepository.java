package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.EdgeDensityTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EdgeDensityTestRepository extends JpaRepository<EdgeDensityTest, Integer> {
    Optional<EdgeDensityTest> findByEdgeId_EdgeId(String edgeId);
}
