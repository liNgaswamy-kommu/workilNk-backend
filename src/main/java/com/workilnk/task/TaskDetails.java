package com.workilnk.task;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "task_details")
@Data
public class TaskDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;

    /* DELIVERY / TRANSPORT RELATED */
    private String vehicleType;     // BIKE / AUTO / CAR
    private String packageSize;     // SMALL / MEDIUM / LARGE

    /* CLEANING RELATED */
    private String cleaningType;    // NORMAL / DEEP
    private String houseType;       // 1BHK / 2BHK
    private Boolean cleaningMaterialsProvided;

    /* WORKFORCE & DURATION (CORE REQUIREMENT) */
    private Integer numberOfWorkers;
    private Integer numberOfDays;
    private Integer numberOfHours;

    // for single-day task (optional)
    private String completionTime;  // e.g. "18:00"

    /* COMMON */
    private Boolean toolsProvidedByWorker;
    private String thingsToBring;

    @Column(columnDefinition = "TEXT")
    private String additionalNotes;
}
