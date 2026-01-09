package com.workilnk.user;

import org.springframework.stereotype.Service;

import com.workilnk.exception.ResourceNotFoundException;
import com.workilnk.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkerDetailsService {

    private final WorkerDetailsRepository workerDetailsRepository;
    private final UserRepository userRepository;

    public WorkerDetails addWorkerDetails(Long userId, WorkerDetails details) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        if (user.getRole() != Role.WORKER) {
        	throw new UnauthorizedException("Only WORKER users can add worker details");
        }

        details.setUser(user);
        return workerDetailsRepository.save(details);
    }

    public WorkerDetails getWorkerDetailsByUserId(Long userId) {
        return workerDetailsRepository.findAll()
                .stream()
                .filter(wd -> wd.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));
    }
}
