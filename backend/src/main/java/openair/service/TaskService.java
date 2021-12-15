package openair.service;

import openair.dto.TaskDTO;
import openair.exception.NotFoundException;
import openair.model.Employee;
import openair.model.Project;
import openair.model.Task;
import openair.model.TimeSheetDay;
import openair.repository.EmployeeRepository;
import openair.repository.ProjectRepository;
import openair.repository.TaskRepository;
import openair.repository.TimeSheetDayRepository;
import openair.service.interfaces.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {

    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private EmployeeRepository employeeRepository;
    private TimeSheetDayRepository timeSheetDayRepository;

    private ProjectService projectService;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,
                       EmployeeRepository employeeRepository, TimeSheetDayRepository timeSheetDayRepository,
                       ProjectService projectService) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.timeSheetDayRepository = timeSheetDayRepository;
        this.projectService = projectService;
    }

    @Override
    public Task findTaskByName(String name) {
        return taskRepository.findTaskByName(name);
    }

    @Override
    public Task addTask(String name, Project project, Employee employee) {
        Task task = new Task();
        task.setProject(project);
        task.setEmployee(employee);
        task.setName(name);

        return taskRepository.save(task);
    }

    @Override
    public List<Task> findAllByProjectId(Long projectId) {
        Project project = projectService.findProjectById(projectId);
        return project.getTasks();
    }

    @Override
    public List<Task> findAllNotLoggedTasksByProjectId(Long projectId) {
        List<Task> tasks = new ArrayList<Task>();
        Project project = projectService.findProjectById(projectId);
        for(Task task : taskRepository.findAll()) {
            if(!checkIsTaskLogged(task)) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    private boolean checkIsTaskLogged(Task task) {
        for(TimeSheetDay timeSheetDay : timeSheetDayRepository.findAll()) {
            if(timeSheetDay.getDate().isEqual(LocalDate.now()) && timeSheetDay.getTask().getId().equals(task.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Task> findAllByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();

        return employee.getTasks();
    }

    @Override
    public Task findById(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if(taskOptional.isPresent())
            return taskOptional.get();

        else
            return null;
    }


    @Override
    public Task addTaskToProject(Long taskId, Long projectId, Long employeeId) {

        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(!optionalProject.isPresent()) {
            throw new NotFoundException(projectId, "Project with ID: " + projectId + " not found.");
        }
        Project project = optionalProject.get();

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if(!optionalEmployee.isPresent()) {
            throw new NotFoundException(employeeId, "Employee with ID: " + employeeId + " not found.");
        }
        Employee employee = optionalEmployee.get();

        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(!optionalTask.isPresent()) {
            throw  new NotFoundException(taskId, "Task with ID: " + taskId + " not found.");
        }
        Task task = optionalTask.get();

        List<Task> taskList = project.getTasks();
        if(!taskList.contains(task))
            taskList.add(task);

        project.setTasks(taskList);
        employee.setTasks(taskList);

        return taskRepository.save(task);
    }


}
