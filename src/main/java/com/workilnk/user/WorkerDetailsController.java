package com.workilnk.user;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerDetailsController {

    private final WorkerDetailsService workerDetailsService;

    // ADD WORKER DETAILS
    @PostMapping("/{userId}/details")
    public WorkerDetails addWorkerDetails(
            @PathVariable Long userId,
            @RequestBody WorkerDetails details) {

        return workerDetailsService.addWorkerDetails(userId, details);
    }

    // GET WORKER DETAILS BY USER ID
    @GetMapping("/{userId}/details")
    public WorkerDetails getWorkerDetails(@PathVariable Long userId) {
        return workerDetailsService.getWorkerDetailsByUserId(userId);
    }
}
