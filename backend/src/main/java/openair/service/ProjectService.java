package openair.service;

import openair.dto.ProjectDTO;
import openair.model.*;
import openair.model.enums.Status;
import openair.repository.*;
import openair.service.interfaces.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        return projectRepository.findByName(name);
    }

    @Override
    public Project addProject(ProjectDTO projectDTO, Admin admin){
        Project project = new Project();

        project.setAdmin(admin);
        project.setName(projectDTO.getName());
        project.setProjectType(projectDTO.getProjectType());

        return projectRepository.save(project);
    }

    @Override
    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId).get();
    }

    public Project addEmployeeToProject(Long employeeId, Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        Employee employee = employeeRepository.findById(employeeId).get();

        List<Employee> employeeList = project.getEmployeeList();
        if(!employeeList.contains(employee))
            employeeList.add(employee);
        project.setEmployeeList(employeeList);

        return projectRepository.save(project);

}
    @Override
    public List<Project> findAllByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user;
        if(userOptional.isPresent()){
            user = userOptional.get();
        }else
            return null;

        if(user.getUserType().name().equals("ROLE_ADMIN")){
            Admin admin = adminRepository.findById(userId).get();
            return admin.getProjects();
        }else if (user.getUserType().name().equals("ROLE_EMPLOYEE")){
            Employee employee = employeeRepository.findById(userId).get();
            return employee.getProjects();
        }else
            return null;
    }

    @Override
    public List<Project> findAllNotRefundedByEmployeeId(Long id) {
        Employee employee = employeeRepository.findById(id).get();
        List<Project> projects = new ArrayList<Project>();
        for(Project project : employee.getProjects()) {
            if(!checkIsRefunded(project.getId(), employee.getId())) {
                projects.add(project);
            }
        }
        return projects;
    }

    public List<Project> listAll() {
        return projectRepository.findAll(Sort.by("projectType"));
    }

    private boolean checkIsRefunded(Long projectId, Long employeeId) {
        for(ExpenseReport expenseReport : expenseReportRepository.findAll()) {
            if(expenseReport.getEmployee().getId().equals(employeeId)) {
                if(expenseReport.getProject().getId().equals(projectId)) {
                    if(expenseReport.getStatus() == Status.APPROVED || expenseReport.getStatus() == Status.INPROCESS) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
