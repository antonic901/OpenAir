package openair.role;

import openair.model.Role;
import openair.model.enums.UserType;

public class TestDataRole {

    public static Role createRole() {
        Role role = new Role(1L, UserType.ROLE_EMPLOYEE);
        return role;
    }

}