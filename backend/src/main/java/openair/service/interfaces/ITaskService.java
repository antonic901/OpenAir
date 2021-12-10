package openair.service.interfaces;

import openair.dto.TaskDTO;
import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;

import java.util.List;

public interface ITaskService {
    Task findTaskByName(String name);
    Task addTask(String name, Project project, Employee employee);
    List<Task> findAllByProjectId(Long projectId);
    List<Task> findAllNotLoggedTasksByProjectId(Long projectId);
    Task addTaskToProject(Long taskId, Long projectId, Long employeeId);
    List<Task> findAllByEmployeeId(Long employeeId);
    Task findById(Long taskId);
}
