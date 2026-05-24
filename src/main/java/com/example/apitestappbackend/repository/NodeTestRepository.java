package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.NodeTest;
import com.example.apitestappbackend.DTO.NodeTest.BeaconData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeTestRepository extends JpaRepository<NodeTest, Integer> {
    List<NodeTest> findByMapTest_IdAndXCoordinateGreaterThanEqualAndYCoordinateGreaterThanEqual(
            Integer mapId,
            Double xCoordinate,
            Double yCoordinate
    );

    @Query("""
            SELECT n.id AS id,
                   n.XCoordinate AS xCoordinate,
                   n.YCoordinate AS yCoordinate,
                   n.type AS type,
                   w.name AS wardName
            FROM NodeTest n
            LEFT JOIN WardTest w ON w.mapNode.id = n.id
            WHERE n.mapTest.id = :mapId
              AND (n.type = 'room_entrance' OR w.id IS NOT NULL)
            """)
    List<BeaconData> findLandmarksByMapId(@Param("mapId") Integer mapId);
}
