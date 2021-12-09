package openair.controller;

import openair.dto.TaskAddDto;
import openair.exception.ResourceConflictException;
import openair.model.Project;
import openair.model.Task;
import openair.service.ProjectService;
import openair.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/task", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {
    private TaskService taskService;
    private ProjectService projectService;

    @Autowired
    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    @PostMapping("/addTask")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> addTask(@RequestBody TaskAddDto taskAddDto, Project project1) {
        Task existTask = this.taskService.findTaskByName(taskAddDto.getName());
        if(existTask != null) {
            throw new ResourceConflictException(existTask.getId(), "Task alredy exists.");
        }
        Project project = projectService.findProjectByName(project1.getName());
        Task task = this.taskService.addTask(taskAddDto, project);

        return new ResponseEntity<Task>(task, HttpStatus.CREATED);
    }
}
