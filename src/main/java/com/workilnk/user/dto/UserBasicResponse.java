package com.workilnk.user.dto;

import lombok.Data;

@Data
public class UserBasicResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
}
