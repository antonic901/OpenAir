package openair.controller;

import openair.dto.RequestAbsenceDTO;
import openair.model.*;
import openair.model.enums.Status;
import openair.service.AbsenceService;
import openair.service.AdminService;
import openair.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/absence", produces = MediaType.APPLICATION_JSON_VALUE)
public class AbsenceController {

    private AbsenceService absenceService;
    private EmployeeService employeeService;
    private AdminService adminService;

    @Autowired
    public AbsenceController(AbsenceService absenceService, EmployeeService employeeService, AdminService adminService) {
        this.absenceService = absenceService;
        this.employeeService = employeeService;
        this.adminService = adminService;
    }

    @GetMapping("/get-absences/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Absence>> checkIfUsernameIsAvailable(@PathVariable Long id) {
        return new ResponseEntity<List<Absence>>(absenceService.getAllByUserId(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Absence> addAbsence(@RequestBody RequestAbsenceDTO requestAbsenceDTO, Principal loggedEmployee)  {
        Employee employee = employeeService.findByUsername(loggedEmployee.getName());

        Absence absence = new Absence();
        absence.setPeriod(new Period(requestAbsenceDTO.getStartTime(), requestAbsenceDTO.getEndTime()));
        absence.setStatus(Status.INPROCESS);
        absence.setEmployee(employee);
        absence.setAdmin(employee.getAdmin());

        return new ResponseEntity<>(absenceService.add(absence, employee.getId()), HttpStatus.CREATED);
    }

    @PostMapping("/approve/{id}/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Absence> approveAbsence(@PathVariable Long id, @PathVariable Status status, Principal loggedAdmin)  {
        Admin admin = adminService.findByUsername(loggedAdmin.getName());
        Absence absence = absenceService.review(id, status);
        return new ResponseEntity<Absence>(absence, HttpStatus.OK);
    }

}
