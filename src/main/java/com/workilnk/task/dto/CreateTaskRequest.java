package com.workilnk.task.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.workilnk.task.TaskCategory;
import com.workilnk.task.TaskPriority;

import lombok.Data;

@Data
public class CreateTaskRequest {

    private String title;
    private String description;

    private TaskCategory category;
    private TaskPriority priority;

    // Location
    private String fromLocation;   // optional
    private String toLocation;     // mandatory

    // Budget range
    private BigDecimal minBudget;
    private BigDecimal maxBudget;

    // Date range
    private LocalDate startDate;
    private LocalDate endDate;
    
    private String endTime;

    // Category / time specific details
    private TaskDetailsRequest details;
}
