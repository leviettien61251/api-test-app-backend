package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.SetUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetUserInfoRepository extends JpaRepository<SetUserInfo, String> {

}
