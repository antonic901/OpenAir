package openair.controller;

import openair.dto.RegisterEmployeeDTO;
import openair.dto.UserBasicInformationDTO;
import openair.exception.NotFoundException;
import openair.exception.ResourceConflictException;
import openair.model.*;
import openair.service.*;
import openair.utils.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private AdminService adminService;
    private UserService userService;
    private RoleService roleService;

    private ModelMapper modelMapper;

    @Autowired
    public AdminController(AdminService adminService, UserService userService, RoleService roleService, ModelMapper modelMapper) {
        this.adminService = adminService;
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addUser(@RequestBody RegisterEmployeeDTO registerEmployeeDTO, Principal loggedAdmin, UriComponentsBuilder ucBuilder) {

        User existUser = this.userService.findByUsername(registerEmployeeDTO.getUsername());

        if (existUser != null) {
            throw new ResourceConflictException(existUser.getId(), "Username already exists");
        }

        Employee employee = new Employee();
        modelMapper.map(registerEmployeeDTO, employee);
        Admin admin = adminService.findByUsername(loggedAdmin.getName());
        employee.setAdmin(admin);


        Role role = roleService.findByName(registerEmployeeDTO.getUserType());
        if(role == null)
            throw new NotFoundException("Role with user type: " + registerEmployeeDTO.getUserType() + " not found");

        employee.getRoles().add(role);
        registerEmployeeDTO.setAdminId(admin.getId());

        adminService.registerEmployee(employee);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/employee/{userId}").buildAndExpand(employee.getId()).toUri());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/get-employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserBasicInformationDTO>> addUser(Principal loggedAdmin) {
        Admin admin = adminService.findByUsername(loggedAdmin.getName());
        List<Employee> employees = adminService.getEmployees(admin.getId());
        List<UserBasicInformationDTO> response = ObjectMapperUtils.mapAll(employees, UserBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
