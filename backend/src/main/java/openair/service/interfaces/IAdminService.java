package openair.service.interfaces;

import openair.model.Employee;
import openair.model.Admin;

import java.util.List;

public interface IAdminService {
    Employee registerEmployee(Employee employee);
    Admin findByUsername(String username);
    List<Employee> getEmployees(Long id);
}
