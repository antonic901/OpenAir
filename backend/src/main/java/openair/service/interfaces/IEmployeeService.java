package openair.service.interfaces;

import openair.model.Employee;
import openair.model.Project;

import java.util.List;

public interface IEmployeeService {
    Employee add(Employee employee);
    Employee findEmployeeById(Long employeeId);
    List<Employee> findAllByProjectId(Long projectId);
    Employee findByUsername(String name);
    List<Project> findAllProjects();
}
