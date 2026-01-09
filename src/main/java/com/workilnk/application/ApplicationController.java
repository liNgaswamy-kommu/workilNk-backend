package com.workilnk.application;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.workilnk.application.dto.ApplicationListResponse;
import com.workilnk.application.dto.ApplyTaskRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply")
    public Application applyForTask(
            @RequestParam Long taskId,
            @RequestParam Long workerId,
            @RequestBody ApplyTaskRequest request) {

        return applicationService.applyForTask(taskId, workerId, request);
    }
    
    @GetMapping("/task/{taskId}")
    public List<ApplicationListResponse> getApplicationsByTask(
            @PathVariable Long taskId) {

        return applicationService.getApplicationsByTask(taskId);
    }
    
    @PostMapping("/{applicationId}/accept")
    public String acceptApplication(@PathVariable Long applicationId) {

        applicationService.acceptApplication(applicationId);
        return "Application accepted and worker assigned";
    }


}
