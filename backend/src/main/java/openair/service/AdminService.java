package openair.service;

import openair.dto.RegisterEmployeeDTO;
import openair.exception.NotFoundException;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.Role;
import openair.model.enums.UserType;
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
    public Employee registerEmployee(RegisterEmployeeDTO registerEmployeeDTO) {
        Employee employee = new Employee();
        employee.setName(registerEmployeeDTO.getName());
        employee.setSurname(registerEmployeeDTO.getSurname());
        employee.setEmail(registerEmployeeDTO.getEmail());
        employee.setUsername(registerEmployeeDTO.getUsername());
        employee.setPassword(passwordEncoder.encode(registerEmployeeDTO.getPassword()));
        employee.setPhone(registerEmployeeDTO.getPhone());
        employee.setUserType(registerEmployeeDTO.getUserType());

        Role role = roleRepository.findByName(registerEmployeeDTO.getUserType());
        if(role == null) {
            throw new NotFoundException("Role with UserType: " + registerEmployeeDTO.getUserType() + " doesn't exist.");
        }
        employee.getRoles().add(role);

        employee.setDepartment(registerEmployeeDTO.getDepartment());
        employee.setSalary(registerEmployeeDTO.getSalary());

        Optional<Admin> optionalAdmin = adminRepository.findById(registerEmployeeDTO.getAdminId());
        if(!optionalAdmin.isPresent()) {
            throw new NotFoundException(registerEmployeeDTO.getAdminId(), "User with ID: " + registerEmployeeDTO.getAdminId() + " is not found.");
        }
        Admin admin = optionalAdmin.get();
        employee.setAdmin(admin);

        //Osam slobodnih radnih dana pri registraciji
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
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if(!optionalAdmin.isPresent()) {
            throw new NotFoundException(id, "User with ID: " + id + " is not found.");
        }
        Admin admin = optionalAdmin.get();
        return admin.getEmployeeList();
    }
}
