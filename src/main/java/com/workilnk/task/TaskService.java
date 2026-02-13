package com.workilnk.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workilnk.exception.BadRequestException;
import com.workilnk.exception.ResourceNotFoundException;
import com.workilnk.exception.UnauthorizedException;
import com.workilnk.task.dto.CreateTaskRequest;
import com.workilnk.task.dto.TaskListResponse;
import com.workilnk.task.dto.TaskResponse;
import com.workilnk.task.handler.TaskCategoryHandler;
import com.workilnk.user.User;
import com.workilnk.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final Map<TaskCategory, TaskCategoryHandler> handlerMap;

    /* ================= CREATE TASK ================= */

    public TaskResponse createTask(Long userId, CreateTaskRequest request) {

    	System.err.println(request.getEndTime());
        // 🔐 User check
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // ✅ Location validation
        if (request.getToLocation() == null || request.getToLocation().isBlank()) {
            throw new BadRequestException("To location is mandatory");
        }

        // ✅ Budget validation
        if (request.getMinBudget() == null || request.getMaxBudget() == null) {
            throw new BadRequestException("Min and Max budget are required");
        }
        if (request.getMinBudget().compareTo(request.getMaxBudget()) > 0) {
            throw new BadRequestException("Min budget cannot be greater than Max budget");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCategory(request.getCategory());
        task.setPriority(request.getPriority());

        task.setFromLocation(request.getFromLocation());
        task.setToLocation(request.getToLocation());

        task.setMinBudget(request.getMinBudget());
        task.setMaxBudget(request.getMaxBudget());

        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());;

        task.setPostedBy(user);
        task.setStatus(TaskStatus.POSTED);
        task.setCreatedAt(LocalDateTime.now());

        // 🔹 Task details
        TaskDetails details = new TaskDetails();
        details.setTask(task);
        task.setTaskDetails(details);

        // ✅ Time validation (same-day task)
        if (isSameDay(task.getStartDate(), task.getEndDate())) {
            if (request.getDetails() == null ||
                request.getDetails().getCompletionTime() == null) {
                throw new BadRequestException("Completion time required for same-day task");
            }
            details.setCompletionTime(request.getDetails().getCompletionTime());
        }

        // Category specific handler (if exists)
        TaskCategoryHandler handler = handlerMap.get(request.getCategory());
        if (handler != null) {
            handler.validate(request);
            handler.populateDetails(task, request);
        }

        return mapToResponse(taskRepository.save(task));
    }

    /* ================= LIST TASKS ================= */

    public List<TaskListResponse> getAvailableTasks() {
        return taskRepository.findByStatus(TaskStatus.POSTED)
                .stream()
                .map(task -> {
                    TaskListResponse res = new TaskListResponse();
                    res.setId(task.getId());
                    res.setTitle(task.getTitle());
                    res.setDescription(task.getDescription());
                    res.setCategory(task.getCategory());
                    res.setFromLocation(task.getFromLocation());
                    res.setToLocation(task.getToLocation());
                    res.setMinBudget(task.getMinBudget());
                    res.setMaxBudget(task.getMaxBudget());
                    res.setStartDate(task.getStartDate());
                    res.setEndDate(task.getEndDate());
                    res.setStatus(task.getStatus());
                    return res;
                }).collect(Collectors.toList());
    }

    public List<TaskListResponse> getTasksByUser(Long userId) {
        return taskRepository.findByPostedById(userId)
                .stream()
                .map(task -> {
                    TaskListResponse res = new TaskListResponse();
                    res.setId(task.getId());
                    res.setTitle(task.getTitle());
                    res.setDescription(task.getDescription());
                    res.setCategory(task.getCategory());
                    res.setFromLocation(task.getFromLocation());
                    res.setToLocation(task.getToLocation());
                    res.setMinBudget(task.getMinBudget());
                    res.setMaxBudget(task.getMaxBudget());
                    res.setStartDate(task.getStartDate());
                    res.setEndDate(task.getEndDate());
                    res.setStatus(task.getStatus());
                    return res;
                }).collect(Collectors.toList());
    }

    /* ================= UPDATE TASK ================= */

    public TaskResponse updateTask(Long taskId, Long userId, CreateTaskRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getPostedBy().getId().equals(userId))
            throw new UnauthorizedException("You can edit only your task");

        if (task.getStatus() != TaskStatus.POSTED)
            throw new BadRequestException("Only POSTED tasks can be edited");

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCategory(request.getCategory());
        task.setPriority(request.getPriority());

        task.setFromLocation(request.getFromLocation());
        task.setToLocation(request.getToLocation());

        task.setMinBudget(request.getMinBudget());
        task.setMaxBudget(request.getMaxBudget());

        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());

        return mapToResponse(taskRepository.save(task));
    }
    
    /* ================= DELETE TASK ================= */
    
    public void deleteTask(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getPostedBy().getId().equals(userId)) {
            throw new UnauthorizedException("You can delete only your task");
        }

        if (task.getStatus() != TaskStatus.POSTED) {
            throw new BadRequestException("Only POSTED tasks can be deleted");
        }

        taskRepository.delete(task);
    }

    /* ================= GET OPEN TASKS ================= */
    
    public List<TaskResponse> getOpenTasks(Long userId) {

        return taskRepository.findByStatus(TaskStatus.POSTED)
                .stream()
                .filter(task ->
                    task.getPostedBy() != null &&
                    !task.getPostedBy().getId().equals(userId)
                )
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    /* ================= STATUS ACTIONS ================= */

    public void startTask(Long taskId, Long workerId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (task.getStatus() != TaskStatus.ASSIGNED)
            throw new BadRequestException("Task not ready to start");

        if (!task.getAssignedTo().getId().equals(workerId))
            throw new UnauthorizedException("Only assigned worker can start task");

        task.setStatus(TaskStatus.IN_PROGRESS);
        taskRepository.save(task);
    }

    public void completeTask(Long taskId, Long workerId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (task.getStatus() != TaskStatus.IN_PROGRESS)
            throw new BadRequestException("Task not in progress");

        if (!task.getAssignedTo().getId().equals(workerId))
            throw new UnauthorizedException("Only assigned worker can complete task");

        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);
    }

    public void confirmTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (task.getStatus() != TaskStatus.COMPLETED)
            throw new BadRequestException("Task not completed yet");

        if (!task.getPostedBy().getId().equals(userId))
            throw new UnauthorizedException("Only task owner can confirm");

        task.setStatus(TaskStatus.PAID);
        taskRepository.save(task);
    }

    /* ================= MAPPERS ================= */

    private TaskResponse mapToResponse(Task task) {

        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setCategory(task.getCategory().name());
        response.setPriority(task.getPriority());
        response.setFromLocation(task.getFromLocation());
        response.setToLocation(task.getToLocation());
        response.setMinBudget(task.getMinBudget());
        response.setMaxBudget(task.getMaxBudget());
        response.setStartDate(task.getStartDate());
        response.setEndDate(task.getEndDate());
        response.setStatus(task.getStatus().name());
        response.setPostedByUserId(task.getPostedBy().getId());
        response.setPostedByName(task.getPostedBy().getName());

        return response;
    }

    /* ================= UTILS ================= */

    private boolean isSameDay(LocalDate start, LocalDate end) {
        return start != null && end != null && start.isEqual(end);
    }
}
