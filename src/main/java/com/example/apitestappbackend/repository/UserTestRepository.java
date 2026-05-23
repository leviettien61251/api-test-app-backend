package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.hospitaldb.UserTest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserTestRepository extends JpaRepository<UserTest, String> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserTest> findByPhoneNumber(String phoneNumber);

    boolean existsByToken(String token);

    Optional<UserTest> findByToken(String token);

    boolean existsByRefreshToken(String refreshToken);

    Optional<UserTest> findByRefreshToken(String refreshToken);

    void deleteByPhoneNumberIn(Collection<String> phoneNumbers);

    @Modifying
    @Transactional
    @Query("SELECT u FROM UserTest u WHERE u.token = ''")
    List<UserTest> findAllWhereTokenIsEmpty();
    
    @Modifying
    @Transactional
    @Query("UPDATE UserTest u SET u.token = :token, u.refreshToken = :refreshToken, u.tokenExpiresAt = :tokenExpiresAt WHERE u.phoneNumber = :phoneNumber")
    int updateTokenInfo(@Param("phoneNumber") String phoneNumber,
                        @Param("token") String token,
                        @Param("refreshToken") String refreshToken,
                        @Param("tokenExpiresAt") Timestamp tokenExpiresAt);

    @Modifying
    @Transactional
    @Query("UPDATE UserTest u SET u.token = '', u.refreshToken = '', u.tokenExpiresAt = null WHERE u.phoneNumber = :phoneNumber")
    int clearTokenInfo(@Param("phoneNumber") String phoneNumber);


    boolean existsByPassword(String password);
}
