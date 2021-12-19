package openair.controller;

import openair.dto.ProjectBasicInformationDTO;
import openair.dto.UserBasicInformationDTO;
import openair.model.Project;
import openair.model.User;
import openair.service.AdminService;
import openair.service.ProjectService;
import openair.service.UserService;
import openair.utils.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;
    private ModelMapper modelMapper;
    private ProjectService projectService;

    @Autowired
    public UserController(UserService userService,ModelMapper modelMapper,ProjectService projectService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.projectService = projectService;

    }

    //ovo je putanja za get basic info o useru
    //TODO ako se dobavljaju podaci o ulogovanom korisniku onda ima smisla dodati autorizaciju
    //jer ne moze pristupiti metodi ako nije ulogovan??
    //i u web config da se doda da toj putanji ne mogu svi da pristupe nego admin i employee samo
    @GetMapping("/{userId}")
    public ResponseEntity<UserBasicInformationDTO> findByUsername(Principal loggedUser) {
        User user = null;
        if(loggedUser != null) {
            user = userService.findByUsername(loggedUser.getName());
        }
        return new ResponseEntity<>(modelMapper.map(user, UserBasicInformationDTO.class), HttpStatus.OK);
    }

    //vraca liste projekata i za admina i za employee-a
    @GetMapping("/{userId}/projects")
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public ResponseEntity<List<ProjectBasicInformationDTO>> findAllProjectsByUserId(@PathVariable Long userId) {
        List<Project> projects = projectService.findAllByUserId(userId);
        List<ProjectBasicInformationDTO> response = ObjectMapperUtils.mapAll(projects, ProjectBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}/not-refunded-projects")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<ProjectBasicInformationDTO>> findAllNotRefundedProjectsByUserId(@PathVariable Long userId) {
        List<Project> projects = projectService.findAllNotRefundedByEmployeeId(userId);
        List<ProjectBasicInformationDTO> response = ObjectMapperUtils.mapAll(projects, ProjectBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
