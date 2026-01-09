package com.workilnk.task.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateTaskRequest {

    private String title;
    private String description;
    private String category;
    private String location;
    private BigDecimal budget;
    private LocalDate deadline;
}
