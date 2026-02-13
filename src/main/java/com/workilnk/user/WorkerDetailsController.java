package com.workilnk.user;

import org.springframework.web.bind.annotation.*;

import com.workilnk.user.dto.WorkerDetailsResponse;
import com.workilnk.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerDetailsController {

    private final WorkerDetailsService workerDetailsService;

    // ADD WORKER DETAILS
//    @PostMapping("/{userId}/details")
//    public WorkerDetails addWorkerDetails(
//            @PathVariable Long userId,
//            @RequestBody WorkerDetails details) {
//
//        return workerDetailsService.addWorkerDetails(userId, details);
//    }
    
    @PostMapping("/me/details")
    public WorkerDetails addWorkerDetails(@RequestBody WorkerDetails details) {
        Long userId = SecurityUtil.getLoggedInUserId();
        return workerDetailsService.addWorkerDetails(userId, details);
    }


    // GET WORKER DETAILS BY USER ID
    @GetMapping("/{userId}/details")
    public WorkerDetailsResponse getWorkerDetails(@PathVariable Long userId) {
        return workerDetailsService.getWorkerDetailsByWorkerId(userId);
    }

}
