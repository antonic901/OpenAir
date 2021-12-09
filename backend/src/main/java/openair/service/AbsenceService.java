package openair.service;

import openair.dto.RequestAbsenceDTO;
import openair.model.*;
import openair.model.enums.Status;
import openair.repository.AbsenceRepository;
import openair.repository.AdminRepository;
import openair.repository.EmployeeRepository;
import openair.repository.UserRepository;
import openair.service.interfaces.IAbsenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        else return null;
        if(user.getUserType().name().equals("ROLE_ADMIN")){
            Admin admin = adminRepository.findById(id).get();
            return admin.getAbsences();
        }
        else if (user.getUserType().name().equals("ROLE_EMPLOYEE")){
            Employee employee = employeeRepository.findById(id).get();
            return employee.getAbsences();
        }
        else return null;
    }

    @Override
    public Absence add(RequestAbsenceDTO requestAbsenceDTO, Long id) {
        Employee employee = employeeRepository.findById(id).get();
        Period period = new Period(requestAbsenceDTO.getStartTime(),requestAbsenceDTO.getEndTime());
        Absence absence = new Absence();
        absence.setPeriod(period);
        absence.setEmployee(employee);
        absence.setAdmin(employee.getAdmin());
        absence.setStatus(Status.INPROCESS);
        return absenceRepository.save(absence);
    }

    @Override
    public Absence review(Long id, Status status) {
        Absence absence = absenceRepository.findById(id).get();
        absence.setStatus(status);
        return absenceRepository.save(absence);
    }
}
