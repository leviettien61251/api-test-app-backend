package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.EdgeTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdgeTestRepository extends JpaRepository<EdgeTest, String> {
}
