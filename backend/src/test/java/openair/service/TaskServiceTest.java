package openair.service;

import openair.exception.NotFoundException;
import openair.model.Task;
import openair.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Spy
    @InjectMocks
    private TaskService taskService;

    @Test
    void testFindById() {
        //  given
        Task task = new Task();
        task.setId(1L);

        //  when
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        //  then
        Task taskIsNotNull = taskService.findById(1L);

        assertThat(taskIsNotNull).isNotNull();
        assertThrows(NotFoundException.class, () -> taskService.findById(2L),"Task not found.");
    }

    @Test
    void testFindTaskByName() {
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
    void testAddTask() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        assertThat(taskService.addTask(task)).isNotNull();
    }

    @Test
    void testFindAllByProjectId() {
        List<Task> taskList = TaskTestData.createTaskList();
        List<Task> novaLista = new ArrayList<Task>();
        novaLista.add(taskList.get(1));

        when(taskRepository.findAllByProjectId(1L)).thenReturn(novaLista);

        assertThat(taskService.findAllByProjectId(1L)).hasSize(1);
        assertThat(taskService.findAllByProjectId(10L)).isEmpty();
    }

    @Test
    void testFindAllByEmployeeId() {
        List<Task> taskList = TaskTestData.createTaskList();
        List<Task> novaLista = new ArrayList<>();
        novaLista.add(taskList.get(1));

        when(taskRepository.findAllByEmployeeId(1L)).thenReturn(novaLista);

        assertThat(taskService.findAllByEmployeeId(1L)).hasSize(1);
        assertThat(taskService.findAllByEmployeeId(10L)).isEmpty();
    }
}
