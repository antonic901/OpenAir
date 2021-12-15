package openair.absence;

import openair.dto.RequestAbsenceDTO;
import openair.exception.NotFoundException;
import openair.exception.PeriodConflictException;
import openair.model.*;
import openair.model.enums.Status;
import openair.repository.AbsenceRepository;
import openair.repository.AdminRepository;
import openair.repository.EmployeeRepository;
import openair.repository.UserRepository;
import openair.service.AbsenceService;
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
public class AbsenceTests {

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

        Admin admin = TestData.createAdmin();

        Employee employee = TestData.createEmployee();

        List<Absence> absences = TestData.createAbsences();

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
        Employee employee = TestData.createEmployee();
        List<Absence> absences = TestData.createAbsences();

        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(absenceRepository.findAllByEmployeeId(2L)).thenReturn(absences);
        when(absenceRepository.save(any(Absence.class))).thenReturn(new Absence());

        RequestAbsenceDTO dto1 = new RequestAbsenceDTO(
                LocalDateTime.of(2021, 12,25,0,0,0),
                LocalDateTime.of(2021, 12,30,0,0,0)
        );

        assertThat(absenceService.add(dto1, 2L)).isExactlyInstanceOf(Absence.class);
        assertThrows(NotFoundException.class, () -> absenceService.add(dto1, 3L), "User not found");

        RequestAbsenceDTO dto2 = new RequestAbsenceDTO(
                LocalDateTime.of(2021, 12,7,0,0,0),
                LocalDateTime.of(2021, 12,13,0,0,0)
        );

        assertThrows(PeriodConflictException.class, () -> absenceService.add(dto2, 2L), "Period is in conflict");

    }

    public void review() {
        Absence absence = TestData.createAbsences().get(0);

        when(absenceRepository.findById(1L)).thenReturn(Optional.of(absence));
        when(absenceRepository.save(any(Absence.class))).thenReturn(absence);

        assertThat(absenceService.review(1L, Status.APPROVED)).isExactlyInstanceOf(Absence.class);
        assertThrows(NotFoundException.class, () -> absenceService.review(2L, Status.APPROVED), "Absence doesn't exist");

    }

}
