package openair.service.interfaces;

import openair.dto.RegisterEmployeeDTO;
import openair.model.Admin;
import openair.model.Employee;

import java.util.List;

public interface IAdminService {
    Employee registerEmployee(RegisterEmployeeDTO registerEmployeeDTO);
    Admin findByUsername(String username);
    List<Employee> getEmployees(Long id);
}
