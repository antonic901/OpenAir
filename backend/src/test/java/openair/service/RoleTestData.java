package openair.service;

import openair.model.Role;
import openair.model.enums.UserType;

public class RoleTestData {

    public static Role createRole() {
        Role role = new Role(1L, UserType.ROLE_EMPLOYEE);
        return role;
    }

}
