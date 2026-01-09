package com.workilnk.task;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.workilnk.task.dto.CreateTaskRequest;
import com.workilnk.task.dto.TaskListResponse;
import com.workilnk.task.dto.TaskResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponse createTask(
            @RequestParam Long userId,
            @RequestBody CreateTaskRequest request) {

        return taskService.createTask(userId, request);
    }
    
    @GetMapping
    public List<TaskListResponse> getAvailableTasks() {
        return taskService.getAvailableTasks();
    }
    
    @PostMapping("/{taskId}/start")
    public String startTask(
            @PathVariable Long taskId,
            @RequestParam Long workerId) {

        taskService.startTask(taskId, workerId);
        return "Task started successfully";
    }
    
    @PostMapping("/{taskId}/complete")
    public String completeTask(
            @PathVariable Long taskId,
            @RequestParam Long workerId) {

        taskService.completeTask(taskId, workerId);
        return "Task completed successfully";
    }
    
    @PostMapping("/{taskId}/confirm")
    public String confirmTask(
            @PathVariable Long taskId,
            @RequestParam Long userId) {

        taskService.confirmTask(taskId, userId);
        return "Task confirmed and payment released";
    }

}
