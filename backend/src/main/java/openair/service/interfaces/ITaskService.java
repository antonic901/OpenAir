package openair.service.interfaces;

import openair.dto.TaskDTO;
import openair.model.Project;
import openair.model.Task;

import java.util.List;

public interface ITaskService {
    Task findTaskByName(String name);
    Task addTask(TaskDTO taskDTO, Project project);

    List<Task> findAllByProjectId(Long projectId);
}
