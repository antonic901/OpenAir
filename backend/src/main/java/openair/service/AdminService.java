package openair.service;

import openair.dto.RegisterEmployeeDTO;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.enums.UserType;
import openair.repository.AdminRepository;
import openair.service.interfaces.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    private EmployeeService employeeService;
    private RoleService roleService;
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(EmployeeService employeeService, RoleService roleService, AdminRepository adminRepository) {
        this.employeeService = employeeService;
        this.roleService = roleService;
        this.adminRepository = adminRepository;
    }

    @Override
    public Employee registerEmployee(RegisterEmployeeDTO registerEmployeeDTO) {
        Employee employee = new Employee();
        employee.setName(registerEmployeeDTO.getName());
        employee.setSurname(registerEmployeeDTO.getSurname());
        employee.setEmail(registerEmployeeDTO.getEmail());
        employee.setUsername(registerEmployeeDTO.getUsername());
        employee.setPassword(passwordEncoder.encode(registerEmployeeDTO.getPassword()));
        employee.setPhone(registerEmployeeDTO.getPhone());
        employee.setUserType(registerEmployeeDTO.getUserType());
        employee.getRoles().add(roleService.findByName(registerEmployeeDTO.getUserType()));
        employee.setDepartment(registerEmployeeDTO.getDepartment());
        employee.setSalary(registerEmployeeDTO.getSalary());
        employee.setAdmin(adminRepository.findById(registerEmployeeDTO.getAdminId()).get());
        return employeeService.add(employee);
    }

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public List<Employee> getEmployees(Long id) {
        return adminRepository.findById(id).get().getEmployeeList();
    }
}
