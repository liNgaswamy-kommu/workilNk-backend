package com.workilnk.user;

import org.springframework.stereotype.Service;

import com.workilnk.exception.ResourceNotFoundException;
import com.workilnk.user.dto.UserBasicResponse;
import com.workilnk.user.dto.WorkerDetailsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkerDetailsService {

    private final WorkerDetailsRepository workerDetailsRepository;
    private final UserRepository userRepository;

    public WorkerDetails addWorkerDetails(Long userId, WorkerDetails details) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

//        if (user.getRole() != Role.WORKER) {
//        	throw new UnauthorizedException("Only WORKER users can add worker details");
//        }

        details.setUser(user);
        return workerDetailsRepository.save(details);
    }

//    public WorkerDetailsResponse getWorkerDetailsByUserId(Long userId) {
//        return workerDetailsRepository.findAll();
//               .stream()
//               .filter(wd -> wd.getUser().getId().equals(userId))
//                .findFirst()
//                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));
//    }
    
    public WorkerDetailsResponse getWorkerDetailsByWorkerId(Long userId) {

        WorkerDetails worker = workerDetailsRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker details not found"));

        User user = worker.getUser();

        UserBasicResponse userDto = new UserBasicResponse();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());

        WorkerDetailsResponse response = new WorkerDetailsResponse();
        response.setWorkerId(worker.getId());
        response.setSkills(worker.getSkills());
        response.setAvailability(worker.getAvailability());
        response.setBasePrice(worker.getBasePrice());
        response.setRating(worker.getRating());
        response.setVerified(worker.getVerified());
        response.setUser(userDto);

        return response;
    }

}
