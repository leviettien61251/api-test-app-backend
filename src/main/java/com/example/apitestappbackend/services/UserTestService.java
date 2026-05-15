package com.example.apitestappbackend.services;

import com.example.apitestappbackend.repository.UserTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserTestService {
    private final UserTestRepository userTestRepository;

    public UserTestService(UserTestRepository userTestRepository) {
        this.userTestRepository = userTestRepository;
    }

    public String cleanUserTestData() {
        userTestRepository.deleteAllInBatch();
        return "Dọn dẹp dữ liệu UserTest thành công";
    }
}
