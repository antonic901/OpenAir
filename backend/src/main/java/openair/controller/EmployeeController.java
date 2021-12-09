package openair.controller;

import openair.model.Employee;
import openair.model.Project;
import openair.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/employee", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/findAllByProjectId/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Employee>> findAllByProjectId(@PathVariable Long projectId) {

        return new ResponseEntity<>(employeeService.findAllByProjectId(projectId), HttpStatus.OK);
    }
}
