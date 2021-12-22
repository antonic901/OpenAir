package openair.controller;

import openair.dto.TimeBasicInformationDTO;
import openair.dto.TimeSheetDayDTO;
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

@RestController
@RequestMapping(value = "/timesheetdays")
public class TimeSheetDayController {

    private EmployeeService employeeService;
    private TimeSheetDayService timeSheetDayService;
    private TaskService taskService;
    private ModelMapper modelMapper;

    @Autowired
    public TimeSheetDayController(EmployeeService employeeService, TimeSheetDayService timeSheetDayService, TaskService taskService, ModelMapper modelMapper){
        this.employeeService = employeeService;
        this.timeSheetDayService = timeSheetDayService;
        this.taskService = taskService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('EMPLOYEE')")
    public ResponseEntity<TimeBasicInformationDTO> addTimeSheetDayByAdmin(@RequestBody TimeSheetDayDTO timeSheetDayDTO, @PathVariable Long employeeId) {

        TimeSheetDay timeSheetDay = modelMapper.map(timeSheetDayDTO, TimeSheetDay.class);
        timeSheetDay.setId(null);

        Task task = taskService.findById(timeSheetDayDTO.getTaskId());
        timeSheetDay.setTask(task);

        Employee employee = employeeService.findEmployeeById(employeeId);
        timeSheetDay.setEmployee(employee);

        return new ResponseEntity<>(modelMapper.map(timeSheetDayService.addTimeSheetDay(timeSheetDay,employee),TimeBasicInformationDTO.class), HttpStatus.CREATED);
    }

}
