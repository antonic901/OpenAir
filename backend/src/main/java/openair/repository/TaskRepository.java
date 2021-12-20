package openair.repository;

import openair.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findTaskByName(String name);
    Optional<Task> findByProjectIdAndEmployeeId(Long projectId, Long employeeId);
    List<Task> findAllByProjectId(Long id);
    List<Task> findAllByEmployeeId(Long id);
    List<Task> findAllByProjectIdAndEmployeeId(Long projectId, Long employeeId);
}
