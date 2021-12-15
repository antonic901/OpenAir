package openair.service;

import openair.dto.RequestAbsenceDTO;
import openair.exception.PeriodConflictException;
import openair.exception.NotFoundException;
import openair.model.*;
import openair.model.enums.Status;
import openair.repository.AbsenceRepository;
import openair.repository.AdminRepository;
import openair.repository.EmployeeRepository;
import openair.repository.UserRepository;
import openair.service.interfaces.IAbsenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AbsenceService implements IAbsenceService {

    private AbsenceRepository  absenceRepository;
    private EmployeeRepository employeeRepository;
    private AdminRepository adminRepository;
    private UserRepository userRepository;

    @Autowired
    public AbsenceService(AbsenceRepository absenceRepository, EmployeeRepository employeeRepository, AdminRepository adminRepository, UserRepository userRepository) {
        this.absenceRepository = absenceRepository;
        this.employeeRepository = employeeRepository;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Absence> getAllByUserId(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
        }
        else {
            throw new NotFoundException(id, "User with ID: " + id + " is not found.");
        };
        if(user.getUserType().name().equals("ROLE_ADMIN")){
            return absenceRepository.findAllByAdminId(id);
        }
        else if (user.getUserType().name().equals("ROLE_EMPLOYEE")){
            return absenceRepository.findAllByEmployeeId(id);
        }
        else {
            throw new NotFoundException(id, "User with UserType: " + user.getUserType() + " is not found.");
        }
    }

    @Override
    public Absence add(RequestAbsenceDTO requestAbsenceDTO, Long id) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        Employee employee;
        if(employeeOptional.isPresent()) {
            employee = employeeOptional.get();
        }
        else {
            throw new NotFoundException(id, "User with ID: " + id + " is not found.");
        }

        if(requestAbsenceDTO.getStartTime().isAfter(requestAbsenceDTO.getEndTime())) {
            throw new PeriodConflictException(id, "User with ID: " + id + " have selected wrong period (start after end))");
        }

        else if(checkIsAbsenceConflicting(employee, requestAbsenceDTO.getStartTime(), requestAbsenceDTO.getEndTime())) {
            throw new PeriodConflictException(id, "User with ID: " + id + " have requested absence which is in conflict with other absence");
        }

        Period period = new Period(requestAbsenceDTO.getStartTime(),requestAbsenceDTO.getEndTime());
        Absence absence = new Absence();
        absence.setPeriod(period);
        absence.setEmployee(employee);
        absence.setAdmin(employee.getAdmin());
        absence.setStatus(Status.INPROCESS);
        return absenceRepository.save(absence);
    }

    boolean checkIsAbsenceConflicting(Employee employee, LocalDateTime startTime, LocalDateTime endTime) {
        List<Absence> absences = absenceRepository.findAllByEmployeeId(employee.getId());
        for(Absence absence : absences) {
            if(absence.getPeriod().getStartTime().isEqual(startTime) && absence.getPeriod().getEndTime().isEqual(endTime)) {
                return  true;
            }
            if(absence.getStatus() == Status.INPROCESS) {
                if(checkConflict(absence, startTime, endTime)) {
                    return true;
                }
            }
            if(absence.getStatus() == Status.APPROVED && absence.getPeriod().getStartTime().isAfter(LocalDateTime.now())) {
                if(checkConflict(absence, startTime, endTime)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean checkConflict(Absence absence, LocalDateTime startTime, LocalDateTime endTime) {
        if(startTime.isBefore(absence.getPeriod().getEndTime()) && endTime.isAfter(absence.getPeriod().getEndTime())) {
            return  true;
        }
        if(startTime.isBefore(absence.getPeriod().getStartTime()) && endTime.isAfter(absence.getPeriod().getStartTime())) {
            return  true;
        }
        else if(absence.getPeriod().getStartTime().isBefore(startTime) && absence.getPeriod().getEndTime().isAfter(endTime)) {
            return true;
        }
        else return false;
    }

    @Override
    public Absence review(Long id, Status status) {
        Optional<Absence> optionalAbsence = absenceRepository.findById(id);
        Absence absence;
        if(optionalAbsence.isPresent()) {
            absence = optionalAbsence.get();
        }
        else {
            throw new NotFoundException(id, "Absence with ID: " + id + " is not found.");
        }
        absence.setStatus(status);
        return absenceRepository.save(absence);
    }
}
