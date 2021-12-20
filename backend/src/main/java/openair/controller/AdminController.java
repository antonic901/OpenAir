package openair.controller;

import openair.dto.ExpenseBasicInformationDTO;
import openair.dto.UserBasicInformationDTO;
import openair.model.*;
import openair.service.*;
import openair.utils.ObjectMapperUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private AdminService adminService;
    private ExpenseReportService expenseReportService;
    private EmployeeService employeeService;

    @Autowired
    public AdminController(AdminService adminService,ExpenseReportService expenseReportService,
                           EmployeeService employeeService) {
        this.adminService = adminService;
        this.expenseReportService = expenseReportService;
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserBasicInformationDTO>> findAllEmployeesByAdminId(Principal loggedAdmin) {
        Admin admin = adminService.findByUsername(loggedAdmin.getName());
        List<Employee> employees = employeeService.findAllEmployeesByAdminId(admin.getId());
        List<UserBasicInformationDTO> response = ObjectMapperUtils.mapAll(employees, UserBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/expense-reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ExpenseBasicInformationDTO>> findAllExpenseReportsByAdminId(Principal loggedAdmin) {
        Admin admin = adminService.findByUsername(loggedAdmin.getName());
        List<ExpenseReport> expenseReports = expenseReportService.getAllByAdminId(admin.getId());
        List<ExpenseBasicInformationDTO> response = ObjectMapperUtils.mapAll(expenseReports, ExpenseBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
