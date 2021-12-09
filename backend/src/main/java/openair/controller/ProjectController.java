package openair.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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

    @PostMapping("/addProject")
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
}
