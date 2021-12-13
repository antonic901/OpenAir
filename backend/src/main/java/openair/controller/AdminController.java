package openair.controller;

import com.lowagie.text.DocumentException;
import openair.dto.RegisterEmployeeDTO;
import openair.exception.ResourceConflictException;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.Project;
import openair.model.User;
import openair.service.*;
import openair.utils.PdfExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private AdminService adminService;
    private UserService userService;
    private EmployeeService employeeService;
    private TimeSheetDayService timeSheetDayService;
    private ProjectService projectService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> addUser(@RequestBody RegisterEmployeeDTO registerEmployeeDTO, Principal loggedAdmin, UriComponentsBuilder ucBuilder) {

        User existUser = this.userService.findByUsername(registerEmployeeDTO.getUsername());
        if (existUser != null) {
            throw new ResourceConflictException(existUser.getId(), "Username already exists");
        }

        Admin admin = adminService.findByUsername(loggedAdmin.getName());
        registerEmployeeDTO.setAdminId(admin.getId());

        Employee employee = this.adminService.registerEmployee(registerEmployeeDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/employee/{userId}").buildAndExpand(employee.getId()).toUri());
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping("/get-employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Employee>> addUser(Principal loggedAdmin) {

        Admin admin = adminService.findByUsername(loggedAdmin.getName());

        return new ResponseEntity<List<Employee>>(adminService.getEmployees(admin.getId()), HttpStatus.OK);
    }

    @GetMapping("/export-pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=emplyee-project.pdf";

        response.setHeader(headerKey, headerValue);
        List<Employee> employeeList = employeeService.listAll();

        PdfExporter exporter = new PdfExporter(employeeList, timeSheetDayService);
        exporter.export(response);
    }

}
