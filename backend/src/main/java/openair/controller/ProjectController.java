package openair.controller;

import openair.dto.AddEmployeeDTO;
import openair.dto.ProjectDTO;
import openair.exception.ResourceConflictException;
import openair.model.Admin;
import openair.model.Project;
import openair.service.AdminService;
import openair.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    private ProjectService projectService;
    private AdminService adminService;

    @Autowired
    public ProjectController(ProjectService projectService, AdminService adminService) {
        this.projectService = projectService;
        this.adminService = adminService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Project> addProject(@RequestBody ProjectDTO projectDTO, Principal loggedAdmin) {

        Project existProject = this.projectService.findProjectByName(projectDTO.getName());
        if (existProject != null) {
            throw new ResourceConflictException(existProject.getId(), "Project name already exists");
        }

        //nadjem admina po username-u
        Admin admin = adminService.findByUsername(loggedAdmin.getName());

        Project project = this.projectService.addProject(projectDTO,admin);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("/findByName")
    public ResponseEntity<Project> findProjectByName(@RequestBody String name){

        return new ResponseEntity<>(this.projectService.findProjectByName(name), HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<Project> findProjectById(@RequestBody Long projectId){

        return new ResponseEntity<>(this.projectService.findProjectById(projectId), HttpStatus.OK);
    }

    @PostMapping("/addEmployee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Project> addEmployee(@RequestBody AddEmployeeDTO addEmployeeDTO) {

        return new ResponseEntity<>(projectService.addEmployeeToProject(addEmployeeDTO.getEmployeeId(),addEmployeeDTO.getProjectId()), HttpStatus.CREATED);
    }

    //vraca liste projekata i za admina i za employee-a
    @GetMapping("/findAllByUserId/{userId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public ResponseEntity<List<Project>> findAllByUserId(@PathVariable Long userId) {

        return new ResponseEntity<>(projectService.findAllByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/find-all-not-refunded/{userId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<Project>> findAllNotRefunded(@PathVariable Long userId) {

        return new ResponseEntity<>(projectService.findAllNotRefundedByEmployeeId(userId), HttpStatus.OK);
    }

}
