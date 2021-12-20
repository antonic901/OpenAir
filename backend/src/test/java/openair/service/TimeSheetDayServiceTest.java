package openair.service;

import openair.dto.TimeSheetDayDTO;
import openair.model.Employee;
import openair.model.TimeSheetDay;
import openair.repository.EmployeeRepository;
import openair.repository.TaskRepository;
import openair.repository.TimeSheetDayRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TimeSheetDayServiceTest {
    @Mock
    private TimeSheetDayRepository timeSheetDayRepository;

    @Spy
    @InjectMocks
    private TimeSheetDayService timeSheetDayService;

    //  method args: timeSheetDayDTO, Employee (object, not Long id)
    @Test
    public void testAddTimeSheetDay() {
        //  given
        Employee employee = AbsenceTestData.createEmployee();
        TimeSheetDay timeSheetDay = TimeSheetDayTestData.createTimeSheetDay();

        //  when
        when(timeSheetDayRepository.save(any(TimeSheetDay.class))).thenReturn(new TimeSheetDay());

        //  then
        assertThat(timeSheetDayService.addTimeSheetDay(timeSheetDay, employee)).isNotNull();
    }
}
