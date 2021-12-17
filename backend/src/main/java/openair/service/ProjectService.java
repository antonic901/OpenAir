package openair.service;

import openair.dto.ProjectDTO;
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

        Optional<Project> projectOptional = projectRepository.findByName(name);

        if(!projectOptional.isPresent())
            throw new NotFoundException("Project with name " + name + " does not exist.");

        return projectOptional.get();
    }

    @Override
    public Project addProject(Project project, Admin admin) {

        Optional<Project> existProject = projectRepository.findByName(project.getName());

        if (existProject.isPresent())
            throw new ResourceConflictException("Project with name " + project.getName() + " already exists.");

        return projectRepository.save(project);
    }

    @Override
    public Project findProjectById(Long projectId) {

        Optional<Project> projectOptional = projectRepository.findById(projectId);

        if(!projectOptional.isPresent())
            throw new NotFoundException("Project with id " + projectId.toString() + " does not exist.");

        return projectOptional.get();
    }

    @Override
    public Project addEmployeeToProject(Long employeeId, Long projectId) {

        Optional<Project> projectOptional = projectRepository.findById(projectId);
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if(!projectOptional.isPresent()){
            throw new NotFoundException("Project with id " + projectId.toString() + " does not exist.");
        }else if(!employeeOptional.isPresent()){
            throw new NotFoundException("Employee with id " + employeeId.toString() + " does not exist.");
        }

        List<Employee> employeeList = projectOptional.get().getEmployeeList();

        if(!employeeList.contains(employeeOptional.get()))
            employeeList.add(employeeOptional.get());

        projectOptional.get().setEmployeeList(employeeList);

        return projectRepository.save(projectOptional.get());

}
    @Override
    public List<Project> findAllByUserId(Long userId) {

        Optional<User> userOptional = userRepository.findById(userId);

        if(!userOptional.isPresent())
            throw new NotFoundException("User with id " + userId.toString() + " does not exist.");

        if(userOptional.get().getUserType().name().equals("ROLE_ADMIN")){

            Admin admin = adminRepository.findById(userId).get();
            return admin.getProjects();
        }else if (userOptional.get().getUserType().name().equals("ROLE_EMPLOYEE")){

            Employee employee = employeeRepository.findById(userId).get();
            return employee.getProjects();
        }
        return null;
    }

    @Override
    public List<Project> findAllNotRefundedByEmployeeId(Long id) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if(!employeeOptional.isPresent())
            throw new NotFoundException("Employee with id " + id.toString() + " does not exist.");

        List<Project> projects = new ArrayList<>();

        for(Project project : projectRepository.findAllByEmployeeId(id)) {

            if(!checkIsRefunded(project.getId(), employeeOptional.get().getId()))
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
