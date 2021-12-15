package openair.time_sheet_day;

import openair.absence.TestData;
import openair.dto.TimeSheetDayDTO;
import openair.model.Employee;
import openair.model.Task;
import openair.model.TimeSheetDay;
import openair.repository.EmployeeRepository;
import openair.repository.TaskRepository;
import openair.repository.TimeSheetDayRepository;
import openair.service.TaskService;
import openair.service.TimeSheetDayService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TimeSheetDayTests {
    @Mock
    private TimeSheetDayRepository timeSheetDayRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskService taskService;

    @Spy
    @InjectMocks
    private TimeSheetDayService timeSheetDayService;

    //  method args: timeSheetDayDTO, Employee (object, not Long id)
    @Test
    public void testAddTimeSheetDay() {
        //  given
        Employee employee = TestData.createEmployee();
        TimeSheetDayDTO timeSheetDayDTO = TimeSheetDayTestData.createTimeSheetDayDTO();
        //  when
        when(timeSheetDayRepository.save(any(TimeSheetDay.class))).thenReturn(new TimeSheetDay());

        //  then
        assertThat(timeSheetDayService.addTimeSheetDay(timeSheetDayDTO, employee)).isNotNull();
    }
}
