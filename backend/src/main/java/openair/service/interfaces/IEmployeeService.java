package openair.service.interfaces;

import openair.model.Employee;
import openair.model.Project;

import java.util.List;

public interface IEmployeeService {
    Employee findByUsername(String username);
    Employee add(Employee employee);
    Employee findEmployeeById(Long employeeId);
    List<Employee> findAllByProjectId(Long projectId);
}
