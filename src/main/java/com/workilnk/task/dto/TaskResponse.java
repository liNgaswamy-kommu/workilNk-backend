package com.workilnk.task.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.workilnk.task.Task;
import com.workilnk.task.TaskPriority;

import lombok.Data;

@Data
public class TaskResponse {

    private Long id;
    private String title;
    private String description;

    private String category;
    private TaskPriority priority;

    private String fromLocation;
    private String toLocation;

    // Budget range
    private BigDecimal minBudget;
    private BigDecimal maxBudget;

    // Date
    private LocalDate startDate;
    private LocalDate endDate;

    // Workers / Time
    private Integer numberOfWorkers;
    private Integer numberOfDays;
    private Integer numberOfHours;
    private String completionTime;

    private String status;

    private Long postedByUserId;
    private String postedByName;

    public static TaskResponse fromEntity(Task task) {

        TaskResponse response = new TaskResponse();

        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());

        response.setCategory(
            task.getCategory() != null ? task.getCategory().name() : null
        );

        response.setPriority(task.getPriority());

        response.setFromLocation(task.getFromLocation());
        response.setToLocation(task.getToLocation());

        response.setMinBudget(task.getMinBudget());
        response.setMaxBudget(task.getMaxBudget());

        response.setStartDate(task.getStartDate());
        response.setEndDate(task.getEndDate());

        if (task.getTaskDetails() != null) {
            response.setNumberOfWorkers(task.getTaskDetails().getNumberOfWorkers());
            response.setNumberOfDays(task.getTaskDetails().getNumberOfDays());
            response.setNumberOfHours(task.getTaskDetails().getNumberOfHours());
            response.setCompletionTime(task.getTaskDetails().getCompletionTime());
        }

        response.setStatus(task.getStatus().name());

        response.setPostedByUserId(task.getPostedBy().getId());
        response.setPostedByName(task.getPostedBy().getName());

        return response;
    }
}
