package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.MapTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapTestRepository extends JpaRepository<MapTest, String> {

}
