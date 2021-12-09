package openair.service.interfaces;

import openair.model.Employee;

import java.util.List;

public interface IEmployeeService {
    Employee add(Employee employee);

    List<Employee> findAllByProjectId(Long projectId);
}
