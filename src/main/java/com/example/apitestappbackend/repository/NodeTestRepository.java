package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.NodeTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeTestRepository extends JpaRepository<NodeTest, Integer> {

}
