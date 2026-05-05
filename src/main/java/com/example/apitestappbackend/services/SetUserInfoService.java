package com.example.apitestappbackend.services;

import com.example.apitestappbackend.models.SetUserInfo;
import com.example.apitestappbackend.repository.SetUserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetUserInfoService {
    private final SetUserInfoRepository setUserInfoRepository;

    public SetUserInfoService(SetUserInfoRepository setUserInfoRepository) {
        this.setUserInfoRepository = setUserInfoRepository;
    }

    public List<SetUserInfo> findAll(){
        return setUserInfoRepository.findAll();
    }

}
