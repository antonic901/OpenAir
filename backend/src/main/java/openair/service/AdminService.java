package openair.service;

import openair.exception.NotFoundException;
import openair.model.Admin;
import openair.model.Employee;
import openair.repository.AdminRepository;
import openair.repository.EmployeeRepository;
import openair.repository.RoleRepository;
import openair.service.interfaces.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService implements IAdminService {

    private AdminRepository adminRepository;

    private EmployeeRepository employeeRepository;
    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(EmployeeRepository employeeRepository, RoleRepository roleRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Employee registerEmployee(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        employee.setFreeDays(8);
        employee.setDateOfHiring(LocalDate.now());

        return employeeRepository.save(employee);
    }

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public List<Employee> getEmployees(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new NotFoundException(id, "User with ID: " + id + " is not found."));
        return admin.getEmployeeList();
    }
}
