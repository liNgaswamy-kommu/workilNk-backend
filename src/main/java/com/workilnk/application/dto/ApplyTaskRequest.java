package com.workilnk.application.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ApplyTaskRequest {

    private BigDecimal bidAmount;
    private String message;
}
