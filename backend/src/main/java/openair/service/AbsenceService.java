package openair.service;

import openair.exception.PeriodConflictException;
import openair.exception.NotFoundException;
import openair.model.*;
import openair.model.enums.Status;
import openair.repository.AbsenceRepository;
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
    private UserRepository userRepository;

    @Autowired
    public AbsenceService(AbsenceRepository absenceRepository, EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.absenceRepository = absenceRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Absence> getAllByUserId(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isEmpty()) {
            throw new NotFoundException(id, "User with ID: " + id + " is not found.");
        }
        else if(userOptional.get().getUserType().name().equals("ROLE_ADMIN")){
            return absenceRepository.findAllByAdminId(id);
        }
        else if (userOptional.get().getUserType().name().equals("ROLE_EMPLOYEE")){
            return absenceRepository.findAllByEmployeeId(id);
        }
        else {
            throw new NotFoundException(id, "User with UserType: " + userOptional.get().getUserType() + " is not found.");
        }
    }

    @Override
    public Absence add(Absence absence, Long id) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        Employee employee;
        if(employeeOptional.isPresent()) {
            employee = employeeOptional.get();
        }
        else {
            throw new NotFoundException(id, "User with ID: " + id + " is not found.");
        }

        if(absence.getPeriod().getStartTime().isAfter(absence.getPeriod().getEndTime())) {
            throw new PeriodConflictException(id, "User with ID: " + id + " have selected wrong period (start after end))");
        }

        else if(checkIsAbsenceConflicting(employee, absence.getPeriod().getStartTime(), absence.getPeriod().getEndTime())) {
            throw new PeriodConflictException(id, "User with ID: " + id + " have requested absence which is in conflict with other absence");
        }

        Period period = new Period(absence.getPeriod().getStartTime(),absence.getPeriod().getEndTime());
        absence.setPeriod(period);
        absence.setEmployee(employee);
        absence.setAdmin(employee.getAdmin());
        absence.setStatus(Status.INPROCESS);
        return absenceRepository.save(absence);
    }

    boolean checkIsAbsenceConflicting(Employee employee, LocalDateTime startTime, LocalDateTime endTime) {

        List<Absence> absences = absenceRepository.findAllByEmployeeIdAndStatus(employee.getId());
        boolean isConflict = false;
        for(Absence absence : absences) {
            isConflict = checkConflict(absence,startTime,endTime);
        }
        return isConflict;
    }

    boolean checkConflict(Absence absence, LocalDateTime startTime, LocalDateTime endTime) {
        return (absence.getPeriod().getStartTime().isEqual(startTime) && absence.getPeriod().getEndTime().isEqual(endTime)) ||
                (startTime.isBefore(absence.getPeriod().getEndTime()) && endTime.isAfter(absence.getPeriod().getEndTime())) ||
                (startTime.isBefore(absence.getPeriod().getStartTime()) && endTime.isAfter(absence.getPeriod().getStartTime())) ||
                (absence.getPeriod().getStartTime().isBefore(startTime) && absence.getPeriod().getEndTime().isAfter(endTime));
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
