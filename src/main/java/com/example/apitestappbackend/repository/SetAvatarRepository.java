package com.example.apitestappbackend.repository;

import com.example.apitestappbackend.models.SetAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetAvatarRepository extends JpaRepository<SetAvatar, String> {
}
