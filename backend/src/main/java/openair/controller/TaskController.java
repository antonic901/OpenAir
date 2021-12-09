package openair.controller;

import openair.dto.TaskDTO;
import openair.exception.ResourceConflictException;
import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;
import openair.service.ProjectService;
import openair.service.TaskService;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping(value = "/api/task")
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
    public ResponseEntity<Task> addTask(@RequestBody TaskDTO taskDTO) {
        Task existTask = this.taskService.findTaskByName(taskDTO.getName());
        if(existTask != null)
            throw new ResourceConflictException(existTask.getId(), "Task already exists");

        Project project = projectService.findProjectById(taskDTO.getProjectID());
        Task task = this.taskService.addTask(taskDTO, project);

        return new ResponseEntity<Task>(task, HttpStatus.CREATED);
    }


    @GetMapping("/findAllByProjectId/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Task>> findAllByProjectId(@PathVariable Long projectId) {

        return new ResponseEntity<>(taskService.findAllByProjectId(projectId), HttpStatus.OK);
    }

    @GetMapping("/findByName")
    public ResponseEntity<Project> findProjectByName(@RequestBody String name){

        return new ResponseEntity<>(this.projectService.findProjectByName(name), HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<Project> findProjectById(@RequestBody Long projectId){

        return new ResponseEntity<>(this.projectService.findProjectById(projectId), HttpStatus.OK);
    }
    //projectId
    //taskId
    @PostMapping("/addTaskToProject/{projectId}/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> addTaskToProject(@PathVariable Long taskId, @PathVariable Long projectId) {
        return new ResponseEntity<Task>(taskService.addTaskToProject(taskId,projectId), HttpStatus.CREATED);
    }

}
