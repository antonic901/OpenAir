package openair.service.interfaces;

import openair.dto.TaskAddDto;
import openair.model.Project;
import openair.model.Task;

public interface ITaskService {
    Task addTask(TaskAddDto taskAddDto, Project project);
    Task findTaskByName(String name);
}
