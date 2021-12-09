package openair.service.interfaces;

import openair.dto.RegisterEmployeeDTO;
import openair.model.Employee;

import java.util.List;

public interface IAdminService {
    Employee registerEmployee(RegisterEmployeeDTO registerEmployeeDTO);
    List<Employee> getEmployees(Long id);
}
