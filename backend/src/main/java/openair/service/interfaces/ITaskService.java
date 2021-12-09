package openair.service.interfaces;

import openair.dto.TaskDTO;
import openair.model.Project;
import openair.model.Task;

public interface ITaskService {
    Task findTaskByName(String name);
    Task addTask(TaskDTO taskDTO, Project project);
    Task addTaskToProject(Long taskId, Long projectId);
}
