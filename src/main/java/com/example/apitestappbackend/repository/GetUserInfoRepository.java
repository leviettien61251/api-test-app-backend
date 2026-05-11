package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.GetUserInfo;
import com.example.apitestappbackend.models.UserTest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface GetUserInfoRepository extends JpaRepository<GetUserInfo, String> {


}
