package com.example.apitestappbackend.services;

import com.example.apitestappbackend.models.SetAvatar;
import com.example.apitestappbackend.repository.SetAvatarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetAvatarService {
    private final SetAvatarRepository setAvatarRepository;

    public SetAvatarService(SetAvatarRepository setAvatarRepository) {
        this.setAvatarRepository = setAvatarRepository;
    }

    public List<SetAvatar> findAll(){
        return setAvatarRepository.findAll();
    }
}
