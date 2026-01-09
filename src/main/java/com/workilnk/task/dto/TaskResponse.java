package com.workilnk.task.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class TaskResponse {

    private Long id;
    private String title;
    private String category;
    private String location;
    private BigDecimal budget;
    private LocalDate deadline;
    private String status;

    // minimal user info
    private Long postedByUserId;
    private String postedByName;
}
