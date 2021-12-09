package openair.repository;

import openair.model.Role;
import openair.model.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(UserType name);
}
