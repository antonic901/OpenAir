package openair.controller;

import openair.dto.AddEmployeeDTO;
import openair.dto.ProjectBasicInformationDTO;
import openair.dto.ProjectDTO;
import openair.dto.UserBasicInformationDTO;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.Project;
import openair.service.AdminService;
import openair.service.ProjectService;

import openair.utils.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    private ProjectService projectService;
    private AdminService adminService;

    private ModelMapper modelMapper;

    @Autowired
    public ProjectController(ProjectService projectService, AdminService adminService, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.adminService = adminService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addProject(@RequestBody ProjectDTO projectDTO, Principal loggedAdmin) {
        Admin admin = adminService.findByUsername(loggedAdmin.getName());
        Project project = new Project();
        modelMapper.map(projectDTO, project);
        projectService.addProject(project, admin);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/find-by-name")
    public ResponseEntity<ProjectBasicInformationDTO> findProjectByName(@RequestBody String name){
        Project project = projectService.findProjectByName(name);
        return new ResponseEntity<>(modelMapper.map(project,ProjectBasicInformationDTO.class), HttpStatus.OK);
    }

    @GetMapping("/find-by-id")
    public ResponseEntity<ProjectBasicInformationDTO> findProjectById(@RequestBody Long projectId){
        Project project = projectService.findProjectById(projectId);
        return new ResponseEntity<>(modelMapper.map(project,ProjectBasicInformationDTO.class), HttpStatus.OK);
    }

    @PostMapping("/add-employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectBasicInformationDTO> addEmployee(@RequestBody AddEmployeeDTO addEmployeeDTO) {
        Project project = projectService.addEmployeeToProject(addEmployeeDTO.getEmployeeId(),addEmployeeDTO.getProjectId());
        return new ResponseEntity<>(modelMapper.map(project, ProjectBasicInformationDTO.class), HttpStatus.CREATED);
    }

    //vraca liste projekata i za admina i za employee-a
    @GetMapping("/find-all-by-user-id/{userId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public ResponseEntity<List<ProjectBasicInformationDTO>> findAllByUserId(@PathVariable Long userId) {
        List<Project> projects = projectService.findAllByUserId(userId);
        List<ProjectBasicInformationDTO> response = ObjectMapperUtils.mapAll(projects, ProjectBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find-all-not-refunded/{userId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<ProjectBasicInformationDTO>> findAllNotRefunded(@PathVariable Long userId) {
        List<Project> projects = projectService.findAllNotRefundedByEmployeeId(userId);
        List<ProjectBasicInformationDTO> response = ObjectMapperUtils.mapAll(projects, ProjectBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find-all-employees-by-project-id/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserBasicInformationDTO>> findAllByProjectId(@PathVariable Long projectId) {
        List<Employee> employees = projectService.findAllEmployeesByProjectId(projectId);
        List<UserBasicInformationDTO> response = ObjectMapperUtils.mapAll(employees, UserBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
