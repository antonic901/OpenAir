package openair.controller;

import openair.dto.TaskDTO;
import openair.exception.ResourceConflictException;
import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;
import openair.service.EmployeeService;
import openair.service.ProjectService;
import openair.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping(value = "/api/task")
public class TaskController {

    private TaskService taskService;
    private ProjectService projectService;
    private EmployeeService employeeService;

    @Autowired
    public TaskController(TaskService taskService, ProjectService projectService, EmployeeService employeeService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    @PostMapping("/add-task")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> addTask(@RequestBody TaskDTO taskDTO) {
        Task existTask = this.taskService.findTaskByName(taskDTO.getName());
        if(existTask != null)
            throw new ResourceConflictException(existTask.getId(), "Task already exists");

        Project project = projectService.findProjectById(taskDTO.getProjectID());
        Employee employee = employeeService.findEmployeeById(taskDTO.getEmployeeID());
        Task task = this.taskService.addTask(taskDTO.getName(), project, employee);

        System.out.println("Odsada");
        return new ResponseEntity<Task>(task, HttpStatus.CREATED);
    }


    @GetMapping("/find-all-by-project-id/{projectId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public ResponseEntity<List<Task>> findAllByProjectId(@PathVariable Long projectId) {

        return new ResponseEntity<>(taskService.findAllByProjectId(projectId), HttpStatus.OK);
    }

    @GetMapping("/find-all-by-project-employee-id/{projectId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<Task>> findAllByProjectIdEmployeeId(@PathVariable Long projectId, Principal loggedEmployee) {

        Employee employee = employeeService.findByUsername(loggedEmployee.getName());

        return new ResponseEntity<>(taskService.findAllByProjectEmployeeId(projectId,employee.getId()), HttpStatus.OK);
    }

    @GetMapping("/find-all-by-employee-id/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Task>> findAllByEmployeeId(@PathVariable Long employeeId) {

        return new ResponseEntity<>(taskService.findAllByEmployeeId(employeeId), HttpStatus.OK);
    }

    @GetMapping("/find-by-name")
    public ResponseEntity<Project> findProjectByName(@RequestBody String name){

        return new ResponseEntity<>(this.projectService.findProjectByName(name), HttpStatus.OK);
    }

    @GetMapping("/find-by-id")
    public ResponseEntity<Project> findProjectById(@RequestBody Long projectId){

        return new ResponseEntity<Project>(this.projectService.findProjectById(projectId), HttpStatus.OK);
    }

}
