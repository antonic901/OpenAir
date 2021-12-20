package openair.service;

import openair.exception.NotFoundException;
import openair.model.Admin;
import openair.model.Employee;
import openair.repository.AdminRepository;
import openair.service.interfaces.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    private AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
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
