package openair.service;

import openair.exception.NotFoundException;
import openair.exception.ResourceConflictException;
import openair.model.*;
import openair.model.enums.Status;
import openair.repository.*;
import openair.service.interfaces.IProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final ExpenseReportRepository expenseReportRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository,
                          UserRepository userRepository, ExpenseReportRepository expenseReportRepository){
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.expenseReportRepository = expenseReportRepository;
    }

    @Override
    public Project findProjectByName(String name) {
        return projectRepository.findByName(name).orElseThrow(() -> new NotFoundException("Project with name " + name + " does not exist."));
    }

    @Override
    public Project addProject(Project project, Admin admin) {
        projectRepository.findByName(project.getName()).ifPresent(value -> {
            throw new ResourceConflictException("Project with name " + value.getName() + " already exists.");
        });
        project.setAdmin(admin);
        return projectRepository.save(project);
    }

    @Override
    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project with id " + projectId + " does not exist."));
    }

    @Override
    public Project addEmployeeToProject(Long employeeId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project with id " + projectId + " does not exist."));
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NotFoundException("Employee with id " + employeeId + " does not exist."));

        List<Employee> employeeList = project.getEmployeeList();

        if(!employeeList.contains(employee))
            employeeList.add(employee);

        project.setEmployeeList(employeeList);

        return projectRepository.save(project);

}
    @Override
    public List<Project> findAllByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " does not exist."));
        if(user.getUserType().name().equals("ROLE_ADMIN")){
            return projectRepository.findAllByAdminId(userId);
        }else if (user.getUserType().name().equals("ROLE_EMPLOYEE")){
            return projectRepository.findAllByEmployeeId(userId);
        }
        return null;
    }

    @Override
    public List<Project> findAllNotRefundedByEmployeeId(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("Employee with id " + id + " does not exist."));

        List<Project> projects = new ArrayList<>();

        for(Project project : projectRepository.findAllByEmployeeId(id)) {
            if(!checkIsRefunded(project.getId(), employee.getId()))
                projects.add(project);
        }
        return projects;
    }

    @Override
    public List<Employee> findAllEmployeesByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project with id " + projectId + " does not exist."));
        return project.getEmployeeList();
    }

    private boolean checkIsRefunded(Long projectId, Long employeeId) {

        return expenseReportRepository.findByProjectIdAndEmployeeId(projectId,employeeId)
                .filter(report -> report.getStatus() != Status.REJECTED).isPresent();

    }
}
