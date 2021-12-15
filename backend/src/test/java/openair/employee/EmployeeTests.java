package openair.employee;

import com.amazonaws.services.kms.model.NotFoundException;
import openair.model.Employee;
import openair.model.Project;

import openair.repository.EmployeeRepository;
import openair.repository.ProjectRepository;
import openair.service.EmployeeService;
import openair.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EmployeeTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Spy
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private ProjectService projectService;

    @Test
    public void testFindAnEmployeeByEmployeeId() {
        //given
        Employee employee = TestData.createEmployee();

        //when
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        //then
        assertThat(employeeService.findEmployeeById(1L)).isInstanceOf(Employee.class);
        assertThrows(NotFoundException.class, () -> employeeService.findEmployeeById(2L));
    }

    @Test
    public void testFindAllEmployees() {
        //given
        List<Employee> employees = TestData.createEmployeeList();

        //when
        when(employeeRepository.findAll()).thenReturn(employees);

        //then
        assertThat(employeeService.findAll()).hasSize(2);
    }

    @Test
    public void testFindEmployeeByUsername() {
        //given
        Employee employee = TestData.createEmployee();

        //when
        when(employeeRepository.findByUsername("anjica99")).thenReturn(Optional.of(employee));

        //then
        assertThat(employeeService.findByUsername("anjica99")).isInstanceOf(Employee.class);
        assertThrows(NotFoundException.class, () -> employeeService.findByUsername("max93"));
    }

    @Test
    public void testAddEmployee() {
        //given
        Employee employee = TestData.createEmployee();

        //when
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        //then
        assertThat(employeeService.add(employee)).isInstanceOf(Employee.class);
    }

}
