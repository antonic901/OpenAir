package openair.controller;

import com.lowagie.text.DocumentException;
import openair.dto.RegisterEmployeeDTO;
import openair.exception.NotFoundException;
import openair.exception.ResourceConflictException;
import openair.model.*;
import openair.repository.TimeSheetDayRepository;
import openair.service.*;
import openair.utils.PdfExporter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private AdminService adminService;
    private UserService userService;
    private TimeSheetDayRepository timeSheetDayRepository;
    private StorageService storageService;
    private RoleService roleService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService,TimeSheetDayRepository timeSheetDayRepository, StorageService storageService, RoleService roleService) {
        this.adminService = adminService;
        this.userService = userService;
        this.timeSheetDayRepository = timeSheetDayRepository;
        this.storageService = storageService;
        this.roleService = roleService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> addUser(@RequestBody RegisterEmployeeDTO registerEmployeeDTO, Principal loggedAdmin, UriComponentsBuilder ucBuilder) {

        User existUser = this.userService.findByUsername(registerEmployeeDTO.getUsername());

        if (existUser != null) {
            throw new ResourceConflictException(existUser.getId(), "Username already exists");
        }

        /*
        Project project = new Project();
        ModelMapper mm = new ModelMapper();
        mm.map(projectDTO, project);
        * */

        Employee employee = new Employee();
        ModelMapper mm = new ModelMapper();
        mm.map(registerEmployeeDTO, employee);
        Admin admin = adminService.findByUsername(loggedAdmin.getName());
        employee.setAdmin(admin);


        Role role = roleService.findByName(registerEmployeeDTO.getUserType());
        if(role == null)
            throw new NotFoundException("Role with user type: " + registerEmployeeDTO.getUserType() + " not found");

        employee.getRoles().add(role);
        registerEmployeeDTO.setAdminId(admin.getId());

        this.adminService.registerEmployee(employee);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/employee/{userId}").buildAndExpand(employee.getId()).toUri());
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping("/get-employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Employee>> addUser(Principal loggedAdmin) {

        Admin admin = adminService.findByUsername(loggedAdmin.getName());

        return new ResponseEntity<>(adminService.getEmployees(admin.getId()), HttpStatus.OK);
    }

    @GetMapping("/export-pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> exportPDF(Principal loggedAdmin) throws DocumentException, IOException {

        PdfExporter exporter = new PdfExporter(timeSheetDayRepository, storageService);
        String fileName = exporter.export(loggedAdmin.getName());

        return new ResponseEntity<>(fileName, HttpStatus.OK);
    }
}
