package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.UserTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTestRepository extends JpaRepository<UserTest, String> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserTest> findByPhoneNumber(String phoneNumber);

    boolean existsByToken(String token);

    Optional<UserTest> findByToken(String token);
}
