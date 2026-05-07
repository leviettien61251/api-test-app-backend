package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.LoggedOutUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedOutUserRepository extends JpaRepository<LoggedOutUser, String> {
    boolean existsByInvalidatedToken(String invalidatedToken);

}
