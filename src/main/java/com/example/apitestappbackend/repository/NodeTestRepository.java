package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.NodeTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeTestRepository extends JpaRepository<NodeTest, Integer> {
    List<NodeTest> findByMapTest_IdAndXCoordinateGreaterThanEqualAndYCoordinateGreaterThanEqual(
            Integer mapId,
            Double xCoordinate,
            Double yCoordinate
    );

}
