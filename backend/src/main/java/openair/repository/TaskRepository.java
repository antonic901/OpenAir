package openair.repository;

import openair.model.Project;
import openair.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findTaskByName(String name);
}
