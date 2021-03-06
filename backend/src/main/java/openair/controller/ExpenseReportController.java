package openair.controller;

import openair.dto.ExspenseReportDTO;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.Project;
import openair.model.enums.Status;
import openair.service.EmployeeService;
import openair.service.ExpenseReportService;
import openair.service.ProjectService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/expense-reports", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseReportController {

    private ExpenseReportService expenseReportService;
    private EmployeeService employeeService;
    private ProjectService projectService;
    private ModelMapper modelMapper;

    @Autowired
    public ExpenseReportController(ExpenseReportService expenseReportService, EmployeeService employeeService,
                                    ProjectService projectService, ModelMapper modelMapper) {
        this.expenseReportService = expenseReportService;
        this.employeeService = employeeService;
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> addReport(@RequestBody ExspenseReportDTO expenseReportDTO, Principal loggedEmployee) {
        Employee employee = employeeService.findByUsername(loggedEmployee.getName());
        Project project = projectService.findProjectById(expenseReportDTO.getProjectId());
        ExpenseReport expenseReport = new ExpenseReport();
        modelMapper.map(expenseReportDTO,expenseReport);
        expenseReport.setEmployee(employee);
        expenseReport.setProject(project);
        expenseReportService.addReport(expenseReport);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reviewReport(@PathVariable Long reportId, @RequestBody Status status) {
        expenseReportService.reviewReport(reportId, status);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
