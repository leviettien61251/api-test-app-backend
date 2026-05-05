package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.Logout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoutRepository extends JpaRepository<Logout, String> {
}
