package com.workilnk.application.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ApplicationListResponse {

    private Long applicationId;

    private Long workerId;
    private String workerName;

    private BigDecimal bidAmount;
    private String message;
    private String status;
}
