package openair.service;

import openair.exception.NotFoundException;
import openair.dto.ExspenseReportDTO;
import openair.model.Employee;
import openair.model.ExpenseReport;
import openair.model.enums.Currency;
import openair.model.enums.Status;
import openair.repository.EmployeeRepository;
import openair.repository.ExpenseReportRepository;
import openair.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ExpenseReportServiceTest {
    @Mock
    private ExpenseReportRepository expenseReportRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private MoneyService moneyService;

    @Spy
    @InjectMocks
    private ExpenseReportService expenseReportService;

    @Test
    public void testAddReport(){
        //given
        Employee employee = new Employee();
        ExspenseReportDTO expenseReportDTO1 = ExpenseReportTestData.createReportDTO();
        ExspenseReportDTO expenseReportDTO2 = ExpenseReportTestData.createReportDTO();
        expenseReportDTO2.setProjectId(2L);

        //when
        when(projectRepository.findById(1L)).thenReturn(Optional.of(ProjectTestData.createProject()));
        when(expenseReportRepository.save(any(ExpenseReport.class))).thenReturn(new ExpenseReport());

        //then
        assertThat(expenseReportService.addReport(expenseReportDTO1,employee)).isInstanceOf(ExpenseReport.class);
        assertThrows(NotFoundException.class, () -> expenseReportService.addReport(expenseReportDTO2,employee));
    }


    @Test
    public void reviewReport(){
        //given
        ExpenseReport expenseReport = ExpenseReportTestData.createReport();
        ExpenseReport expenseReport1 = ExpenseReportTestData.createReport();
        expenseReport1.setId(2L);
        expenseReport1.getRefund().setCurrency(Currency.EUR);
        Status status = Status.REJECTED;

        //when
        when(expenseReportRepository.findById(1L)).thenReturn(Optional.of(expenseReport));
        when(expenseReportRepository.findById(2L)).thenReturn(Optional.of(expenseReport1));
        when(expenseReportRepository.save(any(ExpenseReport.class))).thenReturn(new ExpenseReport());
        when(employeeRepository.save(any(Employee.class))).thenReturn(EmployeeTestData.createEmployee());
        when(moneyService.convert(any(String.class),any(String.class),any(String.class),any(Double.class))).thenReturn(1000.0);

        //then
        assertThat(expenseReportService.reviewReport(1L, status)).isInstanceOf(ExpenseReport.class);
        assertThat(expenseReportService.reviewReport(1L, Status.APPROVED)).isInstanceOf(ExpenseReport.class);
        assertThat(expenseReportService.reviewReport(2L, Status.APPROVED)).isInstanceOf(ExpenseReport.class);
        assertThrows(NotFoundException.class, () -> expenseReportService.reviewReport(3L, Status.APPROVED));

    }
}
