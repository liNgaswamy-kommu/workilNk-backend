package com.workilnk.user;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workilnk.task.Task;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String bio;

    private String profilePic;

    private String address;

    private LocalDateTime createdAt = LocalDateTime.now();

    // User can post many tasks
    @OneToMany(mappedBy = "postedBy")
    @JsonIgnore
    private List<Task> postedTasks;

    // User assigned to many tasks (as worker)
    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnore
    private List<Task> assignedTasks;

    // One user → one worker details
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private WorkerDetails workerDetails;

}
