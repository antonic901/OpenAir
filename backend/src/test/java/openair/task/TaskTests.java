package openair.task;

import openair.model.Task;
import openair.repository.TaskRepository;
import openair.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TaskTests {

    @Mock
    private TaskRepository taskRepository;

    @Spy
    @InjectMocks
    private TaskService taskService;

    @Test
    public void testFindTaskById() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        Task taskIsNotNull = taskService.findById(1L);
        Task taskIsNull = taskService.findById(2L);

        assertThat(taskIsNotNull).isNotNull();
        assertThat(taskIsNull).isNull();
    }
}
