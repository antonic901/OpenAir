package openair.service;

import openair.exception.NotFoundException;
import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;
import openair.repository.EmployeeRepository;
import openair.repository.ProjectRepository;
import openair.repository.TaskRepository;
import openair.repository.TimeSheetDayRepository;
import openair.service.interfaces.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

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
    public Task addTask(Task task) { return taskRepository.save(task); }

    @Override
    public List<Task> findAllByProjectId(Long projectId) {
        return taskRepository.findAllByProjectId(projectId);
    }

    @Override
    public List<Task> findAllByProjectEmployeeId(Long projectId, Long employeeId) { return taskRepository.findAllByProjectIdAndEmployeeId(projectId,employeeId); }

    @Override
    public List<Task> findAllByEmployeeId(Long employeeId) {
        return taskRepository.findAllByEmployeeId(employeeId);
    }

    @Override
    public Task findById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task with id " + taskId + " does not exist."));
    }

}
