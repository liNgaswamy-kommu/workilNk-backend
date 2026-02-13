package com.workilnk.task.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.workilnk.task.TaskCategory;
import com.workilnk.task.TaskPriority;
import com.workilnk.task.TaskStatus;

import lombok.Data;

@Data
public class TaskListResponse {

    private Long id;
    private String title;
    private String description;

    private TaskCategory category;
    private TaskPriority priority;

    private String fromLocation;
    private String toLocation;

    // Budget range
    private BigDecimal minBudget;
    private BigDecimal maxBudget;

    private LocalDate startDate;
    private LocalDate endDate;

    private TaskStatus status;
}
