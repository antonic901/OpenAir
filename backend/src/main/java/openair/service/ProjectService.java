package openair.service;

import liquibase.pro.packaged.P;
import openair.dto.ProjectDTO;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.Project;
import openair.repository.EmployeeRepository;
import openair.repository.ProjectRepository;
import openair.service.interfaces.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService implements IProjectService {

    private ProjectRepository projectRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository){
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
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
}
