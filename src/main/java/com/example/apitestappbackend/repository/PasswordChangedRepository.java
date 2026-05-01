package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.PasswordChanged;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordChangedRepository extends JpaRepository<PasswordChanged, String> {
    // kiểm tra mật khẩu cũ có đúng ko
    // kiểm tra phoneNumber đã đăng nhập hay chưa

}
