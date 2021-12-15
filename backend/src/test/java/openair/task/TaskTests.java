package openair.task;

import openair.absence.TestData;
import openair.exception.NotFoundException;
import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;
import openair.repository.EmployeeRepository;
import openair.repository.ProjectRepository;
import openair.repository.TaskRepository;
import openair.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TaskTests {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Spy
    @InjectMocks
    private TaskService taskService;

    @Test
    public void testFindById() {
        //  given
        Task task = new Task();
        task.setId(1L);

        //  when

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        //  then
        Task taskIsNotNull = taskService.findById(1L);
        Task taskIsNull = taskService.findById(2L);

        assertThat(taskIsNotNull).isNotNull();
        assertThat(taskIsNull).isNull();
    }

    @Test
    public void testAddTask() {
        String name = "refactoring";
        Project project = TestDataTask.createProject1();
        Employee employee = TestData.createEmployee();

        when(taskRepository.save(any(Task.class))).thenReturn(new Task());

        assertThat(taskService.addTask(name, project, employee)).isNotNull();
        //  assertThrows(NotFoundException.class, () -> taskService.addTask(name, project, employee), "Not found project");
    }

    @Test
    public void testAddTaskToProject() {
        //  Treba da se zavrsi...
    }
}
