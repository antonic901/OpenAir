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

import java.util.List;

@Service
public class AbsenceService implements IAbsenceService {

    private final AbsenceRepository  absenceRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Autowired
    public AbsenceService(AbsenceRepository absenceRepository, EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.absenceRepository = absenceRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Absence> getAllByUserId(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id, "User with ID: " + id + " is not found."));

        if(user.getUserType().name().equals("ROLE_ADMIN")){
            return absenceRepository.findAllByAdminId(id);
        }

        return absenceRepository.findAllByEmployeeId(id);
    }

    @Override
    public Absence add(Absence absence, Long id) {

        if(absence.getPeriod().getStartTime().isAfter(absence.getPeriod().getEndTime())) {
            throw new PeriodConflictException(id, "User with ID: " + id + " have selected wrong period (start after end))");
        }

        absenceRepository.findAllByEmployeeIdAndStatus(id,absence.getPeriod().getStartTime(),absence.getPeriod().getEndTime()).ifPresent(value -> {
            throw new PeriodConflictException(id, "User with ID: " + id + " have requested absence which is in conflict with absence with ID: " + value.getId() + ".");
        });

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundException(id, "User with ID: " + id + " is not found."));

        Period period = new Period(absence.getPeriod().getStartTime(),absence.getPeriod().getEndTime());
        absence.setPeriod(period);
        absence.setEmployee(employee);
        absence.setAdmin(employee.getAdmin());
        absence.setStatus(Status.INPROCESS);
        return absenceRepository.save(absence);
    }

    @Override
    public Absence review(Long id, Status status) {
        Absence absence = absenceRepository.findById(id).orElseThrow(() -> new NotFoundException(id, "Absence with ID: " + id + " is not found."));
        absence.setStatus(status);
        return absenceRepository.save(absence);
    }
}
