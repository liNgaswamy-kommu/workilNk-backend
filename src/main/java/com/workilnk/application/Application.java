package com.workilnk.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workilnk.task.Task;
import com.workilnk.user.User;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(
    name = "applications",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"task_id", "worker_id"})
    }
)
@Data
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many applications → one task
    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonIgnore
    private Task task;

    // Worker who applied
    @ManyToOne
    @JoinColumn(name = "worker_id")
    @JsonIgnore
    private User worker;

    @Column(name = "bid_amount", precision = 10, scale = 2)
    private BigDecimal bidAmount;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    private LocalDateTime appliedAt = LocalDateTime.now();

}
