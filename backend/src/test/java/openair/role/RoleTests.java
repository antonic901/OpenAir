package openair.role;

import openair.model.Role;
import openair.model.enums.UserType;
import openair.repository.RoleRepository;
import openair.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RoleTests {

    @Mock
    private RoleRepository roleRepository;

    @Spy
    @InjectMocks
    private RoleService roleService;

    @Test
    public void testFindById() {
        Role role = TestData.createRole();

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        assertThat(roleService.findById(1L)).isNotNull();
        assertThrows(RuntimeException.class, () -> roleService.findById(2L), "Role not found");
    }

    @Test
    public void testFindByName() {
        Role role = TestData.createRole();

        when(roleRepository.findByName(UserType.ROLE_EMPLOYEE)).thenReturn(role);

        assertThat(roleService.findByName(UserType.ROLE_EMPLOYEE)).isNotNull();
        assertThat(roleService.findByName(UserType.ROLE_ADMIN)).isNull();
    }

}
