package openair.controller;

import openair.dto.ProjectBasicInformationDTO;
import openair.dto.ProjectDTO;
import openair.dto.TaskBasicInformationDTO;
import openair.dto.UserBasicInformationDTO;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;
import openair.service.AdminService;
import openair.service.ProjectService;

import openair.service.TaskService;
import openair.utils.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    private ProjectService projectService;
    private AdminService adminService;
    private ModelMapper modelMapper;
    private TaskService taskService;

    @Autowired
    public ProjectController(ProjectService projectService, AdminService adminService,
                             ModelMapper modelMapper, TaskService taskService) {
        this.projectService = projectService;
        this.adminService = adminService;
        this.modelMapper = modelMapper;
        this.taskService = taskService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addProject(@RequestBody ProjectDTO projectDTO, Principal loggedAdmin) {
        Admin admin = adminService.findByUsername(loggedAdmin.getName());
        Project project = new Project();
        modelMapper.map(projectDTO, project);
        projectService.addProject(project, admin);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{projectId}/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectBasicInformationDTO> addEmployeeToProject(@PathVariable Long projectId, @RequestBody Long employeeId) {
        Project project = projectService.addEmployeeToProject(employeeId,projectId);
        return new ResponseEntity<>(modelMapper.map(project, ProjectBasicInformationDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserBasicInformationDTO>> findAllEmployeesByProjectId(@PathVariable Long projectId) {
        List<Employee> employees = projectService.findAllEmployeesByProjectId(projectId);
        List<UserBasicInformationDTO> response = ObjectMapperUtils.mapAll(employees, UserBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{projectId}/tasks")
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public ResponseEntity<List<TaskBasicInformationDTO>> findAllTasksByProjectId(@PathVariable Long projectId) {
        List<Task> tasks = taskService.findAllByProjectId(projectId);
        List<TaskBasicInformationDTO> response = ObjectMapperUtils.mapAll(tasks, TaskBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
