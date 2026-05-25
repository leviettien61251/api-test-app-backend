package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.EdgeStatusTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdgeStatusTestRepository extends JpaRepository<EdgeStatusTest, Integer> {
    @Query("""
            SELECT es.edgeId.edgeId
            FROM EdgeStatusTest es
            WHERE es.occupancyRate > 0.8
              AND es.edgeId.edgeId <> :currentEdge
            """)
    List<String> findBlockedEdgeIds(@Param("currentEdge") String currentEdge);
}
