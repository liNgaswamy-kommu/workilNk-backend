package com.workilnk.application;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.workilnk.application.dto.ApplicationListResponse;
import com.workilnk.application.dto.ApplyTaskRequest;
import com.workilnk.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/tasks/{taskId}/apply")
    public Application applyForTask(
            @PathVariable Long taskId,
            @RequestBody ApplyTaskRequest request) {

        Long workerId = SecurityUtil.getLoggedInUserId();
        return applicationService.applyForTask(taskId, workerId, request);
    }

    @GetMapping("/task/{taskId}")
    public List<ApplicationListResponse> getApplicationsByTask(
            @PathVariable Long taskId) {

        Long userId = SecurityUtil.getLoggedInUserId();
        return applicationService.getApplicationsByTask(taskId, userId);
    }

    @PostMapping("/taskId/{taskId}/applicationId/{applicationId}/accept")
    public String acceptApplication(
            @PathVariable Long taskId,
            @PathVariable Long applicationId) {

        Long userId = SecurityUtil.getLoggedInUserId();
        applicationService.acceptApplication(taskId, applicationId, userId);
        return "Application accepted and worker assigned";
    }

    // ===== ADDED =====
    @PostMapping("/applications/{applicationId}/reject")
    public String rejectApplication(@PathVariable Long applicationId) {

        Long userId = SecurityUtil.getLoggedInUserId();
        applicationService.rejectApplication(applicationId, userId);
        return "Application rejected";
    }
    

}
