package openair.controller;

import openair.dto.ExspenseReportDTO;
import openair.dto.ProjectDTO;
import openair.exception.ResourceConflictException;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.Project;
import openair.model.enums.Status;
import openair.service.AdminService;
import openair.service.EmployeeService;
import openair.service.ExpenseReportService;
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
@RequestMapping(value = "/api/expensereport", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseReportController {

    private ExpenseReportService expenseReportService;
    private EmployeeService employeeService;
    private AdminService adminService;
    private ProjectService projectService;

    @Autowired
    public ExpenseReportController(ExpenseReportService expenseReportService, EmployeeService employeeService,
        ProjectService projectService, AdminService adminService){
        this.expenseReportService = expenseReportService;
        this.employeeService = employeeService;
        this.projectService = projectService;
        this.adminService = adminService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ExpenseReport> addReport(@RequestBody ExspenseReportDTO expenseReportDTO, Principal loggedEmployee) {

        Employee employee = employeeService.findByUsername(loggedEmployee.getName());
        Project project = projectService.findProjectById(expenseReportDTO.getProjectId());

        ExpenseReport expenseReport = this.expenseReportService.addReport(expenseReportDTO,employee,project);
        return new ResponseEntity<>(expenseReport, HttpStatus.CREATED);
    }

    @GetMapping("/get-all-for-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ExpenseReport>> getAllByAdminId(Principal loggedAdmin) {

        Admin admin = adminService.findByUsername(loggedAdmin.getName());

        List<ExpenseReport> expenseReports = this.expenseReportService.getAllByAdminId(admin.getId());
        return new ResponseEntity<List<ExpenseReport>>(expenseReports, HttpStatus.OK);
    }

    @PostMapping("/review/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseReport> reviewReport(@PathVariable Long reportId, @PathVariable Status status) {

        ExpenseReport expenseReport = this.expenseReportService.reviewReport(reportId, status);
        return new ResponseEntity<>(expenseReport, HttpStatus.OK);
    }

}
