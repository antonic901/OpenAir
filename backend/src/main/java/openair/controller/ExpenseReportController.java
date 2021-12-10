package openair.controller;

import openair.dto.ExspenseReportDTO;
import openair.dto.ProjectDTO;
import openair.exception.ResourceConflictException;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.Project;
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

@RestController
@RequestMapping(value = "/api/expensereport", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseReportController {

    private ExpenseReportService expenseReportService;
    private EmployeeService employeeService;
    private ProjectService projectService;

    @Autowired
    public ExpenseReportController(ExpenseReportService expenseReportService, EmployeeService employeeService,
        ProjectService projectService){
        this.expenseReportService = expenseReportService;
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ExpenseReport> addReport(@RequestBody ExspenseReportDTO expenseReportDTO, Principal loggedEmployee) {

        Employee employee = employeeService.findByUsername(loggedEmployee.getName());
        Project project = projectService.findProjectById(expenseReportDTO.getProjectId());

        ExpenseReport expenseReport = this.expenseReportService.addReport(expenseReportDTO,employee,project);
        return new ResponseEntity<>(expenseReport, HttpStatus.CREATED);
    }

    @PostMapping("/approve/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseReport> approveReport(@PathVariable Long reportId) {

        ExpenseReport expenseReport = this.expenseReportService.approveReport(reportId);
        return new ResponseEntity<>(expenseReport, HttpStatus.OK);
    }

    @PostMapping("/reject/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseReport> rejectReport(@PathVariable Long reportId) {

        ExpenseReport expenseReport = this.expenseReportService.rejectReport(reportId);
        return new ResponseEntity<>(expenseReport, HttpStatus.OK);
    }
}
