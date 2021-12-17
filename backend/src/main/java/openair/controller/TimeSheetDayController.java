package openair.controller;

import openair.dto.ProjectDTO;
import openair.dto.TimeSheetDayDTO;
import openair.exception.ResourceConflictException;
import openair.model.*;
import openair.service.EmployeeService;
import openair.service.TaskService;
import openair.service.TimeSheetDayService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/timesheetday")
public class TimeSheetDayController {

    private EmployeeService employeeService;
    private TimeSheetDayService timeSheetDayService;
    private TaskService taskService;

    @Autowired
    public TimeSheetDayController(EmployeeService employeeService, TimeSheetDayService timeSheetDayService, TaskService taskService){
        this.employeeService = employeeService;
        this.timeSheetDayService = timeSheetDayService;
        this.taskService = taskService;
    }

    @PostMapping("/add-by-employee")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<TimeSheetDay> addDay(@RequestBody TimeSheetDayDTO timeSheetDayDTO, Principal loggedEmployee) {

        //nadjem zaposlenog po username-u
        Employee employee = employeeService.findByUsername(loggedEmployee.getName());

        TimeSheetDay timeSheetDay = new TimeSheetDay();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(timeSheetDayDTO,timeSheetDay);

        Task task = taskService.findById(timeSheetDay.getTask().getId());
        timeSheetDay.setTask(task);

        return new ResponseEntity<>(timeSheetDayService.addTimeSheetDay(timeSheetDay,employee), HttpStatus.CREATED);
    }

    @PostMapping("/add-by-admin/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TimeSheetDay> addDay(@RequestBody TimeSheetDayDTO timeSheetDayDTO, @PathVariable Long employeeId) {

        //nadjem zaposlenog po id-ju
        Employee employee = employeeService.findEmployeeById(employeeId);

        TimeSheetDay timeSheetDay = new TimeSheetDay();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(timeSheetDayDTO,timeSheetDay);

        Task task = taskService.findById(timeSheetDay.getTask().getId());
        timeSheetDay.setTask(task);

        return new ResponseEntity<>(timeSheetDayService.addTimeSheetDay(timeSheetDay,employee), HttpStatus.CREATED);
    }

}
