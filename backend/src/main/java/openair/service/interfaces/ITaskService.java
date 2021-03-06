package openair.service.interfaces;

import openair.model.Task;

import java.util.List;

public interface ITaskService {
    Task findTaskByName(String name);
    Task addTask(Task task);
    List<Task> findAllByProjectId(Long projectId);
    List<Task> findAllByProjectEmployeeId(Long projectId, Long employeeId);
    List<Task> findAllByEmployeeId(Long employeeId);
    Task findById(Long taskId);
}
