package openair.service;

import openair.exception.NotFoundException;
import openair.model.Role;
import openair.model.enums.UserType;
import openair.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Spy
    @InjectMocks
    private RoleService roleService;

    @Test
    void testFindById() {
        Role role = RoleTestData.createRole();

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        assertThat(roleService.findById(1L)).isExactlyInstanceOf(Role.class);
        assertThrows(NotFoundException.class, () -> roleService.findById(2L), "Role not found");
    }

    @Test
    void testFindByName() {
        Role role = RoleTestData.createRole();

        when(roleRepository.findByName(UserType.ROLE_EMPLOYEE)).thenReturn(role);

        assertThat(roleService.findByName(UserType.ROLE_EMPLOYEE)).isNotNull();
        assertThat(roleService.findByName(UserType.ROLE_ADMIN)).isNull();
    }

}
