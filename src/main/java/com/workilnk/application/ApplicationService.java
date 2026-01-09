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
import com.workilnk.user.Role;
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

        if (worker.getRole() != Role.WORKER) {
            throw new UnauthorizedException("Only WORKER can apply");
        }

        Application application = new Application();
        application.setTask(task);
        application.setWorker(worker);
        application.setBidAmount(request.getBidAmount());
        application.setMessage(request.getMessage());
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(LocalDateTime.now());

        return applicationRepository.save(application);
    }
    
    public List<ApplicationListResponse> getApplicationsByTask(Long taskId) {

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
    
    
    @Transactional
    public void acceptApplication(Long applicationId) {

        Application selectedApp = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        Task task = selectedApp.getTask();

        if (task.getStatus() != TaskStatus.POSTED) {
            throw new BadRequestException("Task already assigned");
        }

        // Assign worker to task
        task.setAssignedTo(selectedApp.getWorker());
        task.setStatus(TaskStatus.ASSIGNED);

        // Accept selected application
        selectedApp.setStatus(ApplicationStatus.ACCEPTED);

        // Reject others
        applicationRepository.findByTaskId(task.getId())
                .stream()
                .filter(app -> !app.getId().equals(applicationId))
                .forEach(app -> app.setStatus(ApplicationStatus.REJECTED));

        taskRepository.save(task);
    }


}
