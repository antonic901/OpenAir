package openair.repository;

import openair.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findTaskByName(String name);
    List<Task> findAllByProjectId(Long id);
    List<Task> findAllByEmployeeId(Long id);
    List<Task> findAllByProjectIdAndEmployeeId(Long projectId, Long employeeId);
}
