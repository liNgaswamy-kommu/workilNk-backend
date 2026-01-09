package com.workilnk.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workilnk.exception.BadRequestException;
import com.workilnk.exception.ResourceNotFoundException;
import com.workilnk.exception.UnauthorizedException;
import com.workilnk.task.dto.CreateTaskRequest;
import com.workilnk.task.dto.TaskListResponse;
import com.workilnk.task.dto.TaskResponse;
import com.workilnk.user.User;
import com.workilnk.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(Long userId, CreateTaskRequest request) {

        // 1️⃣ Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2️⃣ Create Task entity
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCategory(request.getCategory());
        task.setLocation(request.getLocation());
        task.setBudget(request.getBudget());
        task.setDeadline(request.getDeadline());

        task.setPostedBy(user);
        task.setStatus(TaskStatus.POSTED);
        task.setCreatedAt(LocalDateTime.now());

        // 3️⃣ Save entity
        Task savedTask = taskRepository.save(task);

        // 4️⃣ Convert Entity → Response DTO
        TaskResponse response = new TaskResponse();
        response.setId(savedTask.getId());
        response.setTitle(savedTask.getTitle());
        response.setCategory(savedTask.getCategory());
        response.setLocation(savedTask.getLocation());
        response.setBudget(savedTask.getBudget());
        response.setDeadline(savedTask.getDeadline());
        response.setStatus(savedTask.getStatus().name());
        response.setPostedByUserId(user.getId());
        response.setPostedByName(user.getName());

        return response;
    }
    
    public List<TaskListResponse> getAvailableTasks() {

        return taskRepository.findByStatus(TaskStatus.POSTED)
                .stream()
                .map(task -> {
                    TaskListResponse dto = new TaskListResponse();
                    dto.setId(task.getId());
                    dto.setTitle(task.getTitle());
                    dto.setCategory(task.getCategory());
                    dto.setLocation(task.getLocation());
                    dto.setBudget(task.getBudget());
                    dto.setStatus(task.getStatus().name());
                    dto.setPostedByName(task.getPostedBy().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public void startTask(Long taskId, Long workerId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (task.getStatus() != TaskStatus.ASSIGNED) {
            throw new BadRequestException("Task not ready to start");
        }

        if (!task.getAssignedTo().getId().equals(workerId)) {
            throw new UnauthorizedException("Only assigned worker can start task");
        }

        task.setStatus(TaskStatus.IN_PROGRESS);
        taskRepository.save(task);
    }
    
    public void completeTask(Long taskId, Long workerId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (task.getStatus() != TaskStatus.IN_PROGRESS) {
            throw new BadRequestException("Task not in progress");
        }

        if (!task.getAssignedTo().getId().equals(workerId)) {
            throw new UnauthorizedException("Only assigned worker can complete task");
        }

        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);
    }
    
    public void confirmTask(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (task.getStatus() != TaskStatus.COMPLETED) {
            throw new BadRequestException("Task not completed yet");
        }

        if (!task.getPostedBy().getId().equals(userId)) {
            throw new UnauthorizedException("Only task owner can confirm");
        }

        task.setStatus(TaskStatus.PAID);
        taskRepository.save(task);
    }



}
