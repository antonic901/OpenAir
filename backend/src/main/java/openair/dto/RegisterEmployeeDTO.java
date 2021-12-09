package openair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.Department;
import openair.model.enums.UserType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterEmployeeDTO {

    private String name;

    private String surname;

    private String email;

    private String username;

    private String password;

    private String phone;

    private UserType userType;

    private Department department;

    private double salary;

    private Long adminId;

}
