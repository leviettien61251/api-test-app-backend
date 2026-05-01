package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.SignupNotYetLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignupNotYetLoginRepository extends JpaRepository<SignupNotYetLogin, String> {
    @Query("select s from SignupNotYetLogin s")
    List<SignupNotYetLogin> findAll_();

    @Query("select s from SignupNotYetLogin s where s.phoneNumber = ?1")
    List<SignupNotYetLogin> existsSignupNotYetLoginByPhone_number(String phone_number);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPassword(String password);
}
