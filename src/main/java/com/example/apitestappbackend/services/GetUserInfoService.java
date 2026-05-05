package com.example.apitestappbackend.services;

import com.example.apitestappbackend.models.GetUserInfo;
import com.example.apitestappbackend.repository.GetUserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUserInfoService {
    private final GetUserInfoRepository getUserInfoRepository;

    public GetUserInfoService(GetUserInfoRepository getUserInfoRepository) {
        this.getUserInfoRepository = getUserInfoRepository;
    }

    public List<GetUserInfo> findAll(){
        return getUserInfoRepository.findAll();
    }
}
