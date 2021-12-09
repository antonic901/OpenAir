package openair.service;

import openair.dto.TaskDTO;
import openair.model.Project;
import openair.model.Task;
import openair.repository.ProjectRepository;
import openair.repository.TaskRepository;
import openair.service.interfaces.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.List;

@Service
public class TaskService implements ITaskService {
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;

    @Autowired
    private ProjectService projectService;

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

    @Override
    public List<Task> findAllByProjectId(Long projectId) {
        Project project = projectService.findProjectById(projectId);
        return project.getTasks();
    }

    @Override
    public Task addTaskToProject(Long taskId, Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        Task task = taskRepository.findById(taskId).get();

        List<Task> taskList = project.getTasks();
        if(!taskList.contains(task))
            taskList.add(task);

        project.setTasks(taskList);

        return taskRepository.save(task);

    }
}
