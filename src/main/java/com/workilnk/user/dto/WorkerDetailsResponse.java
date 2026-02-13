package com.workilnk.user.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WorkerDetailsResponse {

    private Long workerId;
    private String skills;
    private String availability;
    private BigDecimal basePrice;
    private BigDecimal rating;
    private Boolean verified;

    private UserBasicResponse user;
}
