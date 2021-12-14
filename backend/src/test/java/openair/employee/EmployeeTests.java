package openair.employee;

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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
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

//    @Test
//    public void testFindAllByProjectId(){
//        //given
//        Project project = new Project();
//        project.setId(1L);
//
////        List<Project> projects = new ArrayList<>();
////        projects.add(project);
////        when(projectRepository.findAll()).thenReturn(projects);
//
//        //when
//        when(projectRepository.findById(any(Long.class))).thenReturn(Optional.of(project));
//
//        //then
//        List<Employee> employeeList = employeeService.findAllByProjectId(1L);
//
//        assertThat(employeeList).hasSize(2);
//    }

    @Test
    public void testFindEmployeeById() {
        //kreiramo test podatke
        Employee employee = new Employee();
        employee.setId(1L);

        //kazemo sta ce metoda findById vratiti u zavisnosti od toga sta joj prosledimo
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        //testiramo funkcionalnost
        Employee employeeIsNotNull = employeeService.findEmployeeById(1L);
        Employee employeeIsNull = employeeService.findEmployeeById(2L);

        //proverimo da li smo dobili zeljene rezultate
        assertThat(employeeIsNotNull).isNotNull();
        assertThat(employeeIsNull).isNull();
    }
}
