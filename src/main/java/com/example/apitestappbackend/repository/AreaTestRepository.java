package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.AreaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaTestRepository extends JpaRepository<AreaTest, String> {
}
