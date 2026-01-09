package com.workilnk.task.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TaskListResponse {

    private Long id;
    private String title;
    private String category;
    private String location;
    private BigDecimal budget;
    private String status;
    private String postedByName;
}
