package com.workilnk.task;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workilnk.application.Application;
import com.workilnk.user.User;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String category;

    @Column(name = "budget", precision = 10, scale = 2)
    private BigDecimal budget;

    private String location;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.POSTED;

    // User who posted the task
    @ManyToOne
    @JoinColumn(name = "posted_by")
    private User postedBy;

    // Worker assigned to the task
    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    private LocalDateTime createdAt = LocalDateTime.now();

    // One task → many applications
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Application> applications;

    // getters & setters
}
