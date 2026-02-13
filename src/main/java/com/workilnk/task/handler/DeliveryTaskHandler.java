package com.workilnk.task.handler;

import org.springframework.stereotype.Service;

import com.workilnk.exception.BadRequestException;
import com.workilnk.task.Task;
import com.workilnk.task.TaskDetails;
import com.workilnk.task.dto.CreateTaskRequest;

@Service
public class DeliveryTaskHandler implements TaskCategoryHandler {

    @Override
    public void validate(CreateTaskRequest request) {

        if (request.getDetails() == null) {
            throw new BadRequestException("Task details are required for delivery");
        }

        if (request.getDetails().getVehicleType() == null ||
            request.getDetails().getVehicleType().isBlank()) {
            throw new BadRequestException("Vehicle type is required for delivery");
        }

        // packageSize is optional → no hard validation
    }

    @Override
    public void populateDetails(Task task, CreateTaskRequest request) {

        TaskDetails d = task.getTaskDetails();

        d.setVehicleType(request.getDetails().getVehicleType());
        d.setPackageSize(request.getDetails().getPackageSize());
    }
}
