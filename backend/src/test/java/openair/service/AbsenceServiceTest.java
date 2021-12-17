package openair.service;

import openair.dto.RequestAbsenceDTO;
import openair.exception.NotFoundException;
import openair.exception.PeriodConflictException;
import openair.model.*;
import openair.model.enums.Status;
import openair.repository.AbsenceRepository;
import openair.repository.AdminRepository;
import openair.repository.EmployeeRepository;
import openair.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AbsenceServiceTest {

    @Mock
    private AbsenceRepository absenceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private AbsenceService absenceService;

    @Test
    public void testGetAllByUserId() {

        Admin admin = AbsenceTestData.createAdmin();

        Employee employee = AbsenceTestData.createEmployee();

        List<Absence> absences = AbsenceTestData.createAbsences();

        when(userRepository.findById(1L)).thenReturn(Optional.of((User) admin));
        when(userRepository.findById(2L)).thenReturn(Optional.of((User) employee));
        when(absenceRepository.findAllByAdminId(1L)).thenReturn(absences);
        when(absenceRepository.findAllByEmployeeId(2L)).thenReturn(absences);

        List<Absence> list1 = absenceService.getAllByUserId(1L);

        assertThat(list1).hasSize(4);
        assertThrows(NotFoundException.class,() -> absenceService.getAllByUserId(3L), "User not found");

    }

    @Test
    public void testAdd() {
        Employee employee = AbsenceTestData.createEmployee();
        List<Absence> absences = AbsenceTestData.createAbsences();

        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(absenceRepository.findAllByEmployeeId(2L)).thenReturn(absences);
        when(absenceRepository.save(any(Absence.class))).thenReturn(new Absence());

        List<Absence> absenceList = AbsenceTestData.createAbsences();

        assertThrows(NotFoundException.class, () -> absenceService.add(absenceList.get(1), 3L), "User not found");

        assertThrows(PeriodConflictException.class, () -> absenceService.add(absenceList.get(2), 2L), "Period is in conflict");
    }

    @Test
    public void review() {
        Absence absence = AbsenceTestData.createAbsences().get(0);

        when(absenceRepository.findById(1L)).thenReturn(Optional.of(absence));
        when(absenceRepository.save(any(Absence.class))).thenReturn(absence);

        assertThat(absenceService.review(1L, Status.APPROVED)).isExactlyInstanceOf(Absence.class);
        assertThrows(NotFoundException.class, () -> absenceService.review(2L, Status.APPROVED), "Absence doesn't exist");

    }

}
