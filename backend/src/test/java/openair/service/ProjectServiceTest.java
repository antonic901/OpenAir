package openair.service;


import openair.dto.ProjectDTO;
import openair.exception.NotFoundException;
import openair.exception.ResourceConflictException;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.Project;
import openair.model.User;
import openair.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExpenseReportRepository expenseReportRepository;

    @Spy
    @InjectMocks
    private ProjectService projectService;

    @Test
    public void testFindProjectByName(){
        //given
        Project project = ProjectTestData.createProject();

        //when
        when(projectRepository.findByName("OpenAir")).thenReturn(Optional.of(project));

        //then
        assertThat(projectService.findProjectByName("OpenAir")).isInstanceOf(Project.class);
        assertThrows(NotFoundException.class, () -> projectService.findProjectByName("SomeOtherName"));
    }

    @Test
    public void testAddProject(){
        //given
        Project project = ProjectTestData.createProject();
        project.setName("Othername");
        Project project1 = ProjectTestData.createProject();
        Admin admin = new Admin();

        //when
        when(projectRepository.findByName("OpenAir")).thenReturn(Optional.of(ProjectTestData.createProject()));
        when(projectRepository.save(any(Project.class))).thenReturn(new Project());

        //then
        assertThat(projectService.addProject(project,admin)).isInstanceOf(Project.class);
        assertThrows(ResourceConflictException.class, () -> projectService.addProject(project1,admin));
    }

    @Test
    public void testFindProjectById(){
        //given
        Project project = ProjectTestData.createProject();

        //when
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        //then
        assertThat(projectService.findProjectById(1L)).isInstanceOf(Project.class);
        assertThrows(NotFoundException.class, () -> projectService.findProjectById(2L));
    }

    @Test
    public void testFindAllEmployeesByProjectId(){
        //given
        Project project = ProjectTestData.createProject();

        //when
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        //then
        assertThat(projectService.findAllEmployeesByProjectId(1L)).hasSize(2);
        assertThrows(NotFoundException.class, () -> projectService.findAllEmployeesByProjectId(2L));
    }

    @Test
    public void testFindAllByUserId(){
        //given
        User user1 = ProjectTestData.createUser1();
        User user2 = ProjectTestData.createUser2();
        Employee employee = ProjectTestData.createEmployee();

        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(projectRepository.findAllByAdminId(1L)).thenReturn(ProjectTestData.createProjectList());
        when(projectRepository.findAllByEmployeeId(2L)).thenReturn(new ArrayList<Project>());

        //then
        assertThat(projectService.findAllByUserId(1L)).hasSize(2);
        assertThat(projectService.findAllByUserId(2L)).hasSize(0);
        assertThrows(NotFoundException.class, () -> projectService.findAllByUserId(3L));
    }

    @Test
    public void testAddEmployeeToProject(){
        //given
        Project project = ProjectTestData.createProject();
        Employee employee = ProjectTestData.createEmployee1();

        //when
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.findById(4L)).thenReturn(Optional.of(employee));
        when(projectRepository.save(project)).thenReturn(project);

        //then
        assertThat(projectService.addEmployeeToProject(employee.getId(),project.getId())).isInstanceOf(Project.class);
        assertThrows(NotFoundException.class, () -> projectService.addEmployeeToProject(employee.getId(),2L));
        assertThrows(NotFoundException.class, () -> projectService.addEmployeeToProject(3L,project.getId()));
    }

    @Test
    public void testFindAllNotRefunded(){
        //given
        Employee employee = ProjectTestData.createEmployee1();

        //when
        when(employeeRepository.findById(4L)).thenReturn(Optional.of(employee));
        when(expenseReportRepository.findByProjectIdAndEmployeeId(1L,4L)).thenReturn(Optional.of(ProjectTestData.createReport()));
        when(projectRepository.findAllByEmployeeId(4L)).thenReturn(ProjectTestData.createProjectList());

        //then
        assertThat(projectService.findAllNotRefundedByEmployeeId(4L)).hasSize(1);
        assertThrows(NotFoundException.class, () -> projectService.findAllNotRefundedByEmployeeId(2L));
    }
}
