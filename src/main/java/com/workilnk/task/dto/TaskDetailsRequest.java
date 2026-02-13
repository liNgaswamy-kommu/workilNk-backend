package com.workilnk.task.dto;

import lombok.Data;

@Data
public class TaskDetailsRequest {

    /* TRANSPORT / DELIVERY */
    private String vehicleType;
    private String packageSize;

    /* CLEANING */
    private String cleaningType;
    private String houseType;
    private Boolean cleaningMaterialsProvided;

    /* WORKFORCE & DURATION */
    private Integer numberOfWorkers;
    private Integer numberOfDays;
    private Integer numberOfHours;

    // for single-day task
    private String completionTime;   // e.g. "17:30"

    /* COMMON */
    private Boolean toolsProvidedByWorker;
    private String thingsToBring;
    private String additionalNotes;
}
