package openair.service;

import openair.dto.TaskDTO;
import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;
import openair.repository.EmployeeRepository;
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
    private EmployeeRepository employeeRepository;

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
    public Task addTask(TaskDTO taskDTO, Project project, Employee employee) {
        Task task = new Task();
        task.setProject(project);
        task.setEmployee(employee);
        task.setName(taskDTO.getName());

        return taskRepository.save(task);
    }

    @Override
    public List<Task> findAllByProjectId(Long projectId) {
        Project project = projectService.findProjectById(projectId);
        return project.getTasks();
    }

    @Override
    public Task addTaskToProject(Long taskId, Long projectId, Long employeeId) {
        Project project = projectRepository.findById(projectId).get();
        Employee employee = employeeRepository.findById(employeeId).get();
        Task task = taskRepository.findById(taskId).get();

        List<Task> taskList = project.getTasks();
        if(!taskList.contains(task))
            taskList.add(task);

        project.setTasks(taskList);
        employee.setTasks(taskList);

        return taskRepository.save(task);
    }
}
