package openair.service;

import openair.dto.TaskAddDto;
import openair.model.Project;
import openair.model.Task;
import openair.repository.EmployeeRepository;
import openair.repository.TaskRepository;
import openair.service.interfaces.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskService implements ITaskService  {
    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task findTaskByName(String name) {
        return taskRepository.findTaskByName(name);
    }

    @Override
    public Task addTask(TaskAddDto taskAddDto, Project project) {
        Task task = new Task();
        task.setProject(project);
        task.setName(taskAddDto.getName());

        return task;
    }
}
