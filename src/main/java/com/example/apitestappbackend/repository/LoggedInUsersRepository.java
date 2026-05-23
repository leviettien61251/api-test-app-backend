package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.LoggedInUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedInUsersRepository extends JpaRepository<LoggedInUsers, String> {
    boolean existsByPhoneNumber(String phoneNumber);

    void deleteByPhoneNumber(String phoneNumber);

    boolean existsByPassword(String password);

    boolean existsByPhoneNumberAndPassword(String phoneNumber, String password);

    LoggedInUsers findLoggedInUsersByPhoneNumber(String phoneNumber);
}
