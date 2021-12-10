package openair.controller;

import openair.dto.ProjectDTO;
import openair.dto.TimeSheetDayDTO;
import openair.exception.ResourceConflictException;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.Project;
import openair.model.TimeSheetDay;
import openair.service.EmployeeService;
import openair.service.TimeSheetDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/timesheetday")
public class TimeSheetDayController {

    private EmployeeService employeeService;
    private TimeSheetDayService timeSheetDayService;

    @Autowired
    public TimeSheetDayController(EmployeeService employeeService, TimeSheetDayService timeSheetDayService){
        this.employeeService = employeeService;
        this.timeSheetDayService = timeSheetDayService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<TimeSheetDay> addDay(@RequestBody TimeSheetDayDTO timeSheetDayDTO, Principal loggedEmployee) {

        //nadjem zaposlenog po username-u
        Employee employee = employeeService.findByUsername(loggedEmployee.getName());

        TimeSheetDay timeSheetDay = this.timeSheetDayService.addTimeSheetDay(timeSheetDayDTO,employee);
        return new ResponseEntity<>(timeSheetDay, HttpStatus.CREATED);
    }

}
