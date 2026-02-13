package com.workilnk.task;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    /* BASIC INFO */
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskCategory category;

    /* PRIORITY */
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;   // HIGH / MEDIUM / LOW

    /* LOCATION */
    private String fromLocation;     // optional
    private String toLocation;       // mandatory

    /* BUDGET */
    private BigDecimal minBudget;
    private BigDecimal maxBudget;

    /* DATE & TIME */
    private LocalDate startDate;
    private LocalDate endDate;

    /* STATUS */
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.POSTED;

    /* USER RELATIONS */
    @ManyToOne
    @JoinColumn(name = "posted_by")
    private User postedBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    /* AUDIT */
    private LocalDateTime createdAt = LocalDateTime.now();

    // 🎤 Audio description (optional feature)
    private String audioDescriptionUrl;

    /* CATEGORY-SPECIFIC DETAILS */
    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    private TaskDetails taskDetails;
}
