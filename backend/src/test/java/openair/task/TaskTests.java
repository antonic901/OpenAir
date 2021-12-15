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
import java.util.List;
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
    public void testFindTaskByName() {
        Task task = new Task();
        String name1 = "testovi";
        String name2 = "refactoring";
        task.setName(name1);

        when(taskRepository.findTaskByName(name1)).thenReturn(task);
        when(taskRepository.findTaskByName(name2)).thenReturn(null);

        Task taskIsNotNull = taskService.findTaskByName(name1);
        Task taskIsNull = taskService.findTaskByName(name2);

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
    }

    //  method args: Long taskId, Long projectId, Long employeeId
    @Test
    public void testAddTaskToProject() {
        //  Treba da se zavrsi...

        //  given
        List<Task> taskList = TestDataTask.createTaskList();

        Project project = new Project();
        project.setId(1L);
        project.getTasks().add(taskList.get(1));

        Task task1 = new Task();
        task1.setId(1L);

        Employee employee = new Employee();
        employee.setId(2L);

        //  when
        when(taskRepository.save(any(Task.class))).thenReturn(new Task());
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        assertThat(taskService.addTaskToProject(1L, 1L, 2L)).isExactlyInstanceOf(Task.class);
        assertThrows(NotFoundException.class, () -> taskService.addTaskToProject(5L, 1L, 2L), "Task is not found.");
    }
}
