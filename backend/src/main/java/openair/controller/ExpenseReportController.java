package openair.controller;

import openair.dto.ExspenseReportDTO;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.enums.Status;
import openair.service.AdminService;
import openair.service.EmployeeService;
import openair.service.ExpenseReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/expensereport", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseReportController {

    private ExpenseReportService expenseReportService;
    private EmployeeService employeeService;
    private AdminService adminService;

    @Autowired
    public ExpenseReportController(ExpenseReportService expenseReportService, EmployeeService employeeService,
                                    AdminService adminService) {
        this.expenseReportService = expenseReportService;
        this.employeeService = employeeService;
        this.adminService = adminService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ExpenseReport> addReport(@RequestBody ExspenseReportDTO expenseReportDTO, Principal loggedEmployee) {

        Employee employee = employeeService.findByUsername(loggedEmployee.getName());

        return new ResponseEntity<>(expenseReportService.addReport(expenseReportDTO, employee), HttpStatus.CREATED);
    }

    @GetMapping("/get-all-for-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ExpenseReport>> getAllByAdminId(Principal loggedAdmin) {

        Admin admin = adminService.findByUsername(loggedAdmin.getName());

        return new ResponseEntity<>(expenseReportService.getAllByAdminId(admin.getId()), HttpStatus.OK);
    }

    @PostMapping("/review/{reportId}/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseReport> reviewReport(@PathVariable Long reportId, @PathVariable Status status) {

        return new ResponseEntity<>(expenseReportService.reviewReport(reportId, status), HttpStatus.OK);
    }
}
