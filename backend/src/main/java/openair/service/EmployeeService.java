package openair.service;

import openair.dto.RegisterEmployeeDTO;
import openair.model.Employee;
import openair.repository.EmployeeRepository;
import openair.service.interfaces.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements IEmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee add(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Boolean checkIfUsernameIsAvailable(String username) {
        Employee employee = employeeRepository.findByUsername(username);

        if(employee.getUsername().equals(username)){
            return false;
        }

        return true;
    }
}
