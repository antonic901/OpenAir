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

    private ProjectRepository projectRepository;
    private EmployeeRepository employeeRepository;
    private UserRepository userRepository;
    private AdminRepository adminRepository;
    private ExpenseReportRepository expenseReportRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository,
                          UserRepository userRepository, AdminRepository adminRepository, ExpenseReportRepository expenseReportRepository){
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
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
        return projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project with id " + projectId.toString() + " does not exist."));
    }

    @Override
    public Project addEmployeeToProject(Long employeeId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project with id " + projectId.toString() + " does not exist."));
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NotFoundException("Employee with id " + employeeId.toString() + " does not exist."));

        List<Employee> employeeList = project.getEmployeeList();

        if(!employeeList.contains(employee))
            employeeList.add(employee);

        project.setEmployeeList(employeeList);

        return projectRepository.save(project);

}
    @Override
    public List<Project> findAllByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId.toString() + " does not exist."));
        if(user.getUserType().name().equals("ROLE_ADMIN")){
            Admin admin = adminRepository.findById(userId).get();
            return admin.getProjects();
        }else if (user.getUserType().name().equals("ROLE_EMPLOYEE")){
            Employee employee = employeeRepository.findById(userId).get();
            return employee.getProjects();
        }
        return null;
    }

    @Override
    public List<Project> findAllNotRefundedByEmployeeId(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("Employee with id " + id.toString() + " does not exist."));

        List<Project> projects = new ArrayList<>();

        for(Project project : projectRepository.findAllByEmployeeId(id)) {
            if(!checkIsRefunded(project.getId(), employee.getId()))
                projects.add(project);
        }
        return projects;
    }

    @Override
    public List<Employee> findAllEmployeesByProjectId(Long projectId) {

        Optional<Project> projectOptional =  projectRepository.findById(projectId);

        if(!projectOptional.isPresent())
            throw new NotFoundException("Project with id " + projectId.toString() + " does not exist.");

        return projectOptional.get().getEmployeeList();
    }

    private boolean checkIsRefunded(Long projectId, Long employeeId) {

        Optional<ExpenseReport> expenseReport = expenseReportRepository.findByProjectIdAndEmployeeId(projectId,employeeId);

        if(!expenseReport.isPresent())
            return false;

        if(expenseReport.get().getStatus() != Status.REJECTED)
            return true;

        return false;
    }
}
