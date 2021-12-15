package openair.project;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.amazonaws.services.kms.model.NotFoundException;
import openair.dto.ProjectDTO;
import openair.exception.ResourceConflictException;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.Project;
import openair.model.User;
import openair.repository.AdminRepository;
import openair.repository.EmployeeRepository;
import openair.repository.ProjectRepository;
import openair.repository.UserRepository;
import openair.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProjectTests {
    @Mock
    private ProjectRepository projectRepository;

    @Spy
    @InjectMocks
    private ProjectService projectService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindProjectByName(){
        //given
        Project project = TestData.createProject();

        //when
        when(projectRepository.findByName("OpenAir")).thenReturn(Optional.of(project));

        //then
        assertThat(projectService.findProjectByName("OpenAir")).isInstanceOf(Project.class);
        assertThrows(NotFoundException.class, () -> projectService.findProjectByName("SomeOtherName"));
    }

    @Test
    public void testAddProject(){
        //given
        ProjectDTO projectDTO = TestData.createProjectDTO();
        projectDTO.setName("Othername");
        ProjectDTO projectDTO1 = TestData.createProjectDTO();
        Admin admin = new Admin();

        //when
        when(projectRepository.findByName("OpenAir")).thenReturn(Optional.of(TestData.createProject()));
        when(projectRepository.save(any(Project.class))).thenReturn(new Project());

        //then
        assertThat(projectService.addProject(projectDTO,admin)).isInstanceOf(Project.class);
        assertThrows(AlreadyExistsException.class, () -> projectService.addProject(projectDTO1,admin));
    }

    @Test
    public void testFindProjectById(){
        //given
        Project project = TestData.createProject();

        //when
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        //then
        assertThat(projectService.findProjectById(1L)).isInstanceOf(Project.class);
        assertThrows(NotFoundException.class, () -> projectService.findProjectById(2L));
    }

    @Test
    public void testFindAllEmployeesByProjectId(){
        //given
        Project project = TestData.createProject();

        //when
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        //then
        assertThat(projectService.findAllEmployeesByProjectId(1L)).hasSize(2);
        assertThrows(NotFoundException.class, () -> projectService.findAllEmployeesByProjectId(2L));
    }

    @Test
    public void testFindAllByUserId(){
        //given
        User user1 = TestData.createUser1();
        User user2 = TestData.createUser2();
        Admin admin = TestData.createAdmin();
        Employee employee = TestData.createEmployee();

        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));

        //then
        assertThat(projectService.findAllByUserId(1L)).hasSize(2);
        assertThat(projectService.findAllByUserId(2L)).hasSize(2);
        assertThrows(NotFoundException.class, () -> projectService.findAllByUserId(3L));
    }

    @Test
    public void testAddEmployeeToProject(){
        //given
        Project project = TestData.createProject();
        Employee employee = TestData.createEmployee1();

        //when
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.findById(4L)).thenReturn(Optional.of(employee));
        when(projectRepository.save(project)).thenReturn(project);

        //then
        assertThat(projectService.addEmployeeToProject(employee.getId(),project.getId())).isInstanceOf(Project.class);
        assertThrows(NotFoundException.class, () -> projectService.addEmployeeToProject(employee.getId(),2L));
        assertThrows(NotFoundException.class, () -> projectService.addEmployeeToProject(3L,project.getId()));
    }
}
