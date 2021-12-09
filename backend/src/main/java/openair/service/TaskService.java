package openair.service;

import openair.dto.TaskDTO;
import openair.model.Project;
import openair.model.Task;
import openair.repository.TaskRepository;
import openair.service.interfaces.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService implements ITaskService {
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
    public Task addTask(TaskDTO taskDTO, Project project) {
        Task task = new Task();
        task.setProject(project);
        task.setName(taskDTO.getName());

        return taskRepository.save(task);
    }
}
