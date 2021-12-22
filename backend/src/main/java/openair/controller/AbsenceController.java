package openair.controller;

import openair.dto.RequestAbsenceDTO;
import openair.model.*;
import openair.model.enums.Status;
import openair.service.AbsenceService;
import openair.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/absences", produces = MediaType.APPLICATION_JSON_VALUE)
public class AbsenceController {

    private AbsenceService absenceService;
    private EmployeeService employeeService;

    @Autowired
    public AbsenceController(AbsenceService absenceService, EmployeeService employeeService) {
        this.absenceService = absenceService;
        this.employeeService = employeeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> addAbsence(@RequestBody RequestAbsenceDTO requestAbsenceDTO, Principal loggedEmployee)  {
        Employee employee = employeeService.findByUsername(loggedEmployee.getName());

        Absence absence = new Absence();
        absence.setPeriod(new Period(requestAbsenceDTO.getStartTime(), requestAbsenceDTO.getEndTime()));
        absence.setStatus(Status.INPROCESS);
        absence.setEmployee(employee);
        absence.setAdmin(employee.getAdmin());

        absenceService.add(absence, employee.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reviewAbsence(@PathVariable Long id, @RequestBody Status status)  {
        absenceService.review(id, status);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
