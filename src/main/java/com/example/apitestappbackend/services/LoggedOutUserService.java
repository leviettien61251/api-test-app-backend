package com.example.apitestappbackend.services;

import com.example.apitestappbackend.models.LoggedOutUser;
import com.example.apitestappbackend.repository.LoggedOutUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoggedOutUserService {
    private final LoggedOutUserRepository loggedOutUserRepository;

    public LoggedOutUserService(LoggedOutUserRepository loggedOutUserRepository) {
        this.loggedOutUserRepository = loggedOutUserRepository;
    }

    public List<LoggedOutUser> findAll() {
        return loggedOutUserRepository.findAll();
    }
}
