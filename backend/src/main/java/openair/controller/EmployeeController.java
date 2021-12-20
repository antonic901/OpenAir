package openair.controller;

import openair.dto.RegisterEmployeeDTO;
import openair.dto.TaskBasicInformationDTO;
import openair.dto.TimeBasicInformationDTO;
import openair.dto.TimeSheetDayDTO;
import openair.exception.NotFoundException;
import openair.exception.ResourceConflictException;
import openair.model.*;
import openair.service.*;

import openair.utils.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {

    private EmployeeService employeeService;
    private AdminService adminService;
    private UserService userService;
    private RoleService roleService;
    private ModelMapper modelMapper;
    private TaskService taskService;
    private TimeSheetDayService timeSheetDayService;

    @Autowired
    public EmployeeController(TimeSheetDayService timeSheetDayService,TaskService taskService,AdminService adminService, EmployeeService employeeService, UserService userService, RoleService roleService, ModelMapper modelMapper) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.adminService = adminService;
        this.taskService = taskService;
        this.timeSheetDayService = timeSheetDayService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addEmployee(@RequestBody RegisterEmployeeDTO registerEmployeeDTO, Principal loggedAdmin, UriComponentsBuilder ucBuilder) {

        User existUser = this.userService.findByUsername(registerEmployeeDTO.getUsername());

        if (existUser != null) {
            throw new ResourceConflictException(existUser.getId(), "Username already exists");
        }

        Employee employee = new Employee();
        modelMapper.map(registerEmployeeDTO, employee);
        Admin admin = adminService.findByUsername(loggedAdmin.getName());
        employee.setAdmin(admin);


        Role role = roleService.findByName(registerEmployeeDTO.getUserType());
        if(role == null)
            throw new NotFoundException("Role with user type: " + registerEmployeeDTO.getUserType() + " not found");

        employee.getRoles().add(role);
        registerEmployeeDTO.setAdminId(admin.getId());

        adminService.registerEmployee(employee);
        //HttpHeaders headers = new HttpHeaders();
        //headers.setLocation(ucBuilder.path("/api/employee/{userId}").buildAndExpand(employee.getId()).toUri());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{employeeId}/tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskBasicInformationDTO>> findAllTasksByEmployeeId(@PathVariable Long employeeId) {
        List<Task> tasks = taskService.findAllByEmployeeId(employeeId);
        List<TaskBasicInformationDTO> response = ObjectMapperUtils.mapAll(tasks, TaskBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{employeeId}/timesheetdays")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TimeBasicInformationDTO> addTimeSheetDayForEmployee(@RequestBody TimeSheetDayDTO timeSheetDayDTO, @PathVariable Long employeeId) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        TimeSheetDay timeSheetDay = new TimeSheetDay();
        Task task = taskService.findById(timeSheetDay.getTask().getId());
        timeSheetDay.setTask(task);
        return new ResponseEntity<>(modelMapper.map(timeSheetDayService.addTimeSheetDay(timeSheetDay,employee), TimeBasicInformationDTO.class), HttpStatus.CREATED);
    }
}
