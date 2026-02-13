package com.workilnk.task.handler;

import com.workilnk.task.Task;
import com.workilnk.task.dto.CreateTaskRequest;

/**
 * Category-specific handler.
 *
 * Responsibility:
 * - Validate category-specific TaskDetails data
 * - Populate TaskDetails only
 *
 * Must NOT handle:
 * - Location
 * - Budget
 * - Priority
 * - Dates
 */
public interface TaskCategoryHandler {

    void validate(CreateTaskRequest request);

    void populateDetails(Task task, CreateTaskRequest request);
}
