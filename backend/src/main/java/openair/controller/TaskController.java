package openair.controller;

import openair.dto.TaskBasicInformationDTO;
import openair.dto.TaskDTO;
import openair.exception.ResourceConflictException;
import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;
import openair.service.EmployeeService;
import openair.service.ProjectService;
import openair.service.TaskService;
import openair.utils.ObjectMapperUtils;
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
    public ResponseEntity addTask(@RequestBody TaskDTO taskDTO) {
        Task existTask = this.taskService.findTaskByName(taskDTO.getName());
        if(existTask != null)
            throw new ResourceConflictException(existTask.getId(), "Task already exists");

        Project project = projectService.findProjectById(taskDTO.getProjectID());
        Employee employee = employeeService.findEmployeeById(taskDTO.getEmployeeID());

        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setEmployee(employee);
        task.setProject(project);

        taskService.addTask(task);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/find-all-by-project-id/{projectId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public ResponseEntity<List<TaskBasicInformationDTO>> findAllByProjectId(@PathVariable Long projectId) {
        List<Task> tasks = taskService.findAllByProjectId(projectId);
        List<TaskBasicInformationDTO> response = ObjectMapperUtils.mapAll(tasks, TaskBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find-all-by-project-employee-id/{projectId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<TaskBasicInformationDTO>> findAllByProjectIdEmployeeId(@PathVariable Long projectId, Principal loggedEmployee) {
        Employee employee = employeeService.findByUsername(loggedEmployee.getName());
        List<Task> tasks = taskService.findAllByProjectEmployeeId(projectId,employee.getId());
        List<TaskBasicInformationDTO> response = ObjectMapperUtils.mapAll(tasks, TaskBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find-all-by-employee-id/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskBasicInformationDTO>> findAllByEmployeeId(@PathVariable Long employeeId) {
        List<Task> tasks = taskService.findAllByEmployeeId(employeeId);
        List<TaskBasicInformationDTO> response = ObjectMapperUtils.mapAll(tasks, TaskBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
