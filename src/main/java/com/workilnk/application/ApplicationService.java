package com.workilnk.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workilnk.application.dto.ApplicationListResponse;
import com.workilnk.application.dto.ApplyTaskRequest;
import com.workilnk.exception.BadRequestException;
import com.workilnk.exception.ResourceNotFoundException;
import com.workilnk.exception.UnauthorizedException;
import com.workilnk.task.Task;
import com.workilnk.task.TaskRepository;
import com.workilnk.task.TaskStatus;
import com.workilnk.user.User;
import com.workilnk.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // ================= APPLY FOR TASK =================
    public Application applyForTask(
            Long taskId,
            Long workerId,
            ApplyTaskRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (task.getStatus() != TaskStatus.POSTED) {
            throw new BadRequestException("Task not open for applications");
        }

        User worker = userRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        if (task.getPostedBy().getId().equals(workerId)) {
            throw new BadRequestException("You cannot apply for your own task");
        }
        
     // ===== BID VALIDATION =====
        if (request.getBidAmount() == null) {
            throw new BadRequestException("Bid amount is required");
        }

        if (task.getMaxBudget() != null &&
            request.getBidAmount().compareTo(task.getMaxBudget()) > 0) {

            throw new BadRequestException(
                "Bid amount cannot exceed task budget ₹" + task.getMaxBudget()
            );
        }


        // ===== ADDED: duplicate apply protection =====
        applicationRepository
                .findByTaskIdAndWorkerId(taskId, workerId)
                .ifPresent(app -> {
                    throw new BadRequestException("You already applied for this task");
                });

        Application application = new Application();
        application.setTask(task);
        application.setWorker(worker);
        application.setBidAmount(request.getBidAmount());
        application.setMessage(request.getMessage());
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(LocalDateTime.now());

        return applicationRepository.save(application);
    }
    
    public boolean hasApplied(Long taskId, Long workerId) {
        return applicationRepository
                .findByTaskIdAndWorkerId(taskId, workerId)
                .isPresent();
    }


    // ================= GET APPLICATIONS BY TASK =================
    public List<ApplicationListResponse> getApplicationsByTask(Long taskId, Long loggedInUserId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getPostedBy().getId().equals(loggedInUserId)) {
            throw new UnauthorizedException("Only task owner can view applicants");
        }

        return applicationRepository.findByTaskId(taskId)
                .stream()
                .map(app -> {
                    ApplicationListResponse dto = new ApplicationListResponse();
                    dto.setApplicationId(app.getId());
                    dto.setWorkerId(app.getWorker().getId());
                    dto.setWorkerName(app.getWorker().getName());
                    dto.setBidAmount(app.getBidAmount());
                    dto.setMessage(app.getMessage());
                    dto.setStatus(app.getStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ================= ACCEPT APPLICATION =================
    @Transactional
    public void acceptApplication(Long taskId,
                                  Long applicationId,
                                  Long loggedInUserId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getPostedBy().getId().equals(loggedInUserId)) {
            throw new BadRequestException("Only task owner can accept applications");
        }

        Application selectedApp = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!selectedApp.getTask().getId().equals(taskId)) {
            throw new BadRequestException("Application does not belong to this task");
        }

        task.setAssignedTo(selectedApp.getWorker());
        task.setStatus(TaskStatus.ASSIGNED);

        selectedApp.setStatus(ApplicationStatus.ACCEPTED);

        applicationRepository.findByTaskId(taskId)
                .stream()
                .filter(app -> !app.getId().equals(applicationId))
                .forEach(app -> app.setStatus(ApplicationStatus.REJECTED));

        taskRepository.save(task);
    }

    // ================= REJECT APPLICATION (NEW) =================
    @Transactional
    public void rejectApplication(Long applicationId, Long loggedInUserId) {

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        Task task = app.getTask();

        if (!task.getPostedBy().getId().equals(loggedInUserId)) {
            throw new UnauthorizedException("Only task owner can reject applications");
        }

        app.setStatus(ApplicationStatus.REJECTED);
    }
}
