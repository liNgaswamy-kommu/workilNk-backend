package com.workilnk.task.handler;

import org.springframework.stereotype.Service;

import com.workilnk.exception.BadRequestException;
import com.workilnk.task.Task;
import com.workilnk.task.TaskDetails;
import com.workilnk.task.dto.CreateTaskRequest;

@Service
public class ConstructionTaskHandler implements TaskCategoryHandler {

    @Override
    public void validate(CreateTaskRequest request) {

        if (request.getDetails() == null) {
            throw new BadRequestException("Task details are required for construction work");
        }

        if (request.getDetails().getNumberOfWorkers() == null ||
            request.getDetails().getNumberOfWorkers() <= 0) {
            throw new BadRequestException("Number of workers must be greater than zero");
        }

        if (request.getDetails().getNumberOfDays() == null ||
            request.getDetails().getNumberOfDays() <= 0) {
            throw new BadRequestException("Number of days must be greater than zero");
        }

        // numberOfHours is optional → no hard validation
    }

    @Override
    public void populateDetails(Task task, CreateTaskRequest request) {

        TaskDetails d = task.getTaskDetails();

        d.setNumberOfWorkers(request.getDetails().getNumberOfWorkers());
        d.setNumberOfDays(request.getDetails().getNumberOfDays());
        d.setNumberOfHours(request.getDetails().getNumberOfHours());
        d.setThingsToBring(request.getDetails().getThingsToBring());
    }
}
