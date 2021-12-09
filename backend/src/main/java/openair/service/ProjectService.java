package openair.service;

import liquibase.pro.packaged.P;
import openair.dto.ProjectDTO;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.Project;
import openair.model.User;
import openair.repository.AdminRepository;
import openair.repository.EmployeeRepository;
import openair.repository.ProjectRepository;
import openair.repository.UserRepository;
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

    @Autowired
    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository,
                          UserRepository userRepository, AdminRepository adminRepository){
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
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


}
