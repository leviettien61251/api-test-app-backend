package com.example.apitestappbackend.DTO.SetAvatar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetAvatarData {
    private String id;
    private String phoneNumber;
    private String avatarUrl;

}
