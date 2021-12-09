package openair.service.interfaces;

import openair.dto.RegisterEmployeeDTO;
import openair.model.Employee;

public interface IAdminService {
    Employee registerEmployee(RegisterEmployeeDTO registerEmployeeDTO);
}
