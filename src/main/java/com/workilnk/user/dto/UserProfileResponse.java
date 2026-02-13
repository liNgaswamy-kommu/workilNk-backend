package com.workilnk.user.dto;

import lombok.Data;

@Data
public class UserProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String bio;
    private String address;
    private String profilePic;
    private String role;
}
