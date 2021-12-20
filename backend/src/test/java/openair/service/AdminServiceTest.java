package openair.service;

import openair.exception.NotFoundException;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.Role;
import openair.model.enums.UserType;
import openair.repository.AdminRepository;
import openair.repository.EmployeeRepository;
import openair.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmployeeService employeeService;

    @Spy
    @InjectMocks
    private AdminService adminService;

    @Test
    public void testRegisterEmployee() {

        when(passwordEncoder.encode(any(String.class))).thenReturn("##?A4@!");
        when(employeeRepository.save(any(Employee.class))).thenReturn(AdminTestData.createEmployee());

        List<Employee> employeeList = AdminTestData.createEmployees();

        assertThat(employeeService.add(employeeList.get(1))).isNotNull();
    }

    @Test
    public void testFindByUsername() {
        Admin admin = AdminTestData.createAdmin();

        when(adminRepository.findByUsername("admin")).thenReturn(admin);

        assertThat(adminService.findByUsername("admin")).isExactlyInstanceOf(Admin.class);
        assertThat(adminService.findByUsername("admiin")).isNull();
    }

}
