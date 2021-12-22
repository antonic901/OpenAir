package openair.service;

import openair.model.Admin;
import openair.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Spy
    @InjectMocks
    private AdminService adminService;

    @Test
    void testFindByUsername() {
        Admin admin = AdminTestData.createAdmin();

        when(adminRepository.findByUsername("admin")).thenReturn(admin);

        assertThat(adminService.findByUsername("admin")).isExactlyInstanceOf(Admin.class);
        assertThat(adminService.findByUsername("admiin")).isNull();
    }

}
