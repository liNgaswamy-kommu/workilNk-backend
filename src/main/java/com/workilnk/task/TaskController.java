package com.workilnk.task;
	
	import java.util.List;
	
	import org.springframework.web.bind.annotation.*;
	
	import com.workilnk.task.dto.CreateTaskRequest;
	import com.workilnk.task.dto.TaskListResponse;
	import com.workilnk.task.dto.TaskResponse;
	import com.workilnk.util.SecurityUtil;
	
	import lombok.RequiredArgsConstructor;
	
	@RestController
	@RequestMapping("/api/tasks")
	@RequiredArgsConstructor
	public class TaskController {
	
	    private final TaskService taskService;
	
	    @PostMapping
	    public TaskResponse createTask(@RequestBody CreateTaskRequest request) {
	    	System.err.println(request.getEndTime());
	        Long userId = SecurityUtil.getLoggedInUserId();
	        return taskService.createTask(userId, request);
	    }
	    
	    @GetMapping
	    public List<TaskListResponse> getAvailableTasks() {
	        return taskService.getAvailableTasks();
	    }
	    
	    @PutMapping("/{taskId}")
	    public TaskResponse updateTask(
	            @PathVariable Long taskId,
	            @RequestBody CreateTaskRequest request) {

	        Long userId = SecurityUtil.getLoggedInUserId();
	        return taskService.updateTask(taskId, userId, request);
	    }

	    @DeleteMapping("/{taskId}")
	    public String deleteTask(@PathVariable Long taskId) {

	        Long userId = SecurityUtil.getLoggedInUserId();
	        taskService.deleteTask(taskId, userId);
	        return "Task deleted successfully";
	    }

	    
	    @GetMapping("/me/tasks")
	    public List<TaskListResponse> getTasksByUser() {
	
	    	Long userId = SecurityUtil.getLoggedInUserId();
	        return taskService.getTasksByUser(userId);
	    }
	
	    
	    @PostMapping("/{taskId}/start")
	    public String startTask(@PathVariable Long taskId) {
	        Long workerId = SecurityUtil.getLoggedInUserId();
	        taskService.startTask(taskId, workerId);
	        return "Task started successfully";
	    }
	    
	    @PostMapping("/{taskId}/complete")
	    public String completeTask(@PathVariable Long taskId) {
	        Long workerId = SecurityUtil.getLoggedInUserId();
	        taskService.completeTask(taskId, workerId);
	        return "Task completed successfully";
	    }
	    
	    @PostMapping("/{taskId}/confirm")
	    public String confirmTask(@PathVariable Long taskId) {
	        Long userId = SecurityUtil.getLoggedInUserId();
	        taskService.confirmTask(taskId, userId);
	        return "Task confirmed and payment released";
	    }
	    
	 // ✅ OPEN TASKS (other users)
	    @GetMapping("/open")
	    public List<TaskResponse> getOpenTasks() {
	        Long userId = SecurityUtil.getLoggedInUserId();
	        return taskService.getOpenTasks(userId);
	    }
	
	}