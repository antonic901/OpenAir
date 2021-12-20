package openair.service.interfaces;

import openair.model.Employee;

import java.util.List;

public interface IEmployeeService {
    Employee add(Employee employee);
    Employee findEmployeeById(Long employeeId);
    Employee findByUsername(String name);
    List<Employee> findAll();
    List<Employee> findAllEmployeesByAdminId(Long id);
}
