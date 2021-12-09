package openair.service.interfaces;

import openair.model.Role;
import openair.model.enums.UserType;

public interface IRoleService {
    Role findById(Long id);
    Role findByName(UserType name);
}
