package openair.controller;

import openair.dto.AbsenceBasicInformationDTO;
import openair.dto.RequestAbsenceDTO;
import openair.model.*;
import openair.model.enums.Status;
import openair.service.AbsenceService;
import openair.service.AdminService;
import openair.service.EmployeeService;
import openair.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/absences", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AbsenceBasicInformationDTO>> findAllById(@PathVariable Long id) {
        List<Absence> absences = absenceService.getAllByUserId(id);
        List<AbsenceBasicInformationDTO> response = ObjectMapperUtils.mapAll(absences, AbsenceBasicInformationDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity addAbsence(@RequestBody RequestAbsenceDTO requestAbsenceDTO, Principal loggedEmployee)  {
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
    public ResponseEntity reviewAbsence(@PathVariable Long id, @RequestBody Status status, Principal loggedAdmin)  {
        Admin admin = adminService.findByUsername(loggedAdmin.getName());
        Absence absence = absenceService.review(id, status);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
