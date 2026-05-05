package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.GetUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetUserInfoRepository extends JpaRepository<GetUserInfo, String> {
}
