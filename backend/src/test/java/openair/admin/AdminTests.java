package openair.admin;

import openair.exception.NotFoundException;
import openair.model.Admin;
import openair.model.Employee;
import openair.model.Role;
import openair.model.enums.UserType;
import openair.repository.AdminRepository;
import openair.repository.EmployeeRepository;
import openair.repository.RoleRepository;
import openair.service.AdminService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AdminTests {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    @InjectMocks
    private AdminService adminService;

    @Test
    public void testRegisterEmployee() {
        Admin admin = TestData.createAdmin();

        when(passwordEncoder.encode(any(String.class))).thenReturn("##?A4@!");
        when(roleRepository.findByName(UserType.ROLE_EMPLOYEE)).thenReturn(new Role(1L, UserType.ROLE_EMPLOYEE));
        when(roleRepository.findByName(UserType.ROLE_ADMIN)).thenReturn(new Role(1L, UserType.ROLE_ADMIN));
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(employeeRepository.save(any(Employee.class))).thenReturn(TestData.createEmployee());

        assertThat(adminService.registerEmployee(TestData.createRegisterEmployeeDTO1())).isExactlyInstanceOf(Employee.class);
        assertThrows(NotFoundException.class, () -> adminService.registerEmployee(TestData.createRegisterEmployeeDTO2()), "Admin not found");

        assertThrows(NotFoundException.class, () -> adminService.registerEmployee(TestData.createRegisterEmployeeDTO3()), "Role not found");
    }

    @Test
    public void testFindByUsername() {
        Admin admin = TestData.createAdmin();

        when(adminRepository.findByUsername("admin")).thenReturn(admin);

        assertThat(adminService.findByUsername("admin")).isExactlyInstanceOf(Admin.class);
        assertThat(adminService.findByUsername("admiin")).isNull();
    }

    @Test
    public void testGetEmployees() {
        Admin admin = TestData.createAdmin();

        when(adminRepository.findById(1L)).thenReturn(Optional.of((admin)));

        assertThat(adminService.getEmployees(1L)).hasSize(4);
        assertThrows(NotFoundException.class, () -> adminService.getEmployees(2L), "User not found");
    }

}
