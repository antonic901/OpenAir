package openair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.Department;
import openair.model.enums.UserType;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterEmployeeDTO {

    private String name;

    private String surname;

    private String email;

    private String username;

    @Pattern(regexp="^(?=.[0-9])(?=.[a-z])(?=.[A-Z])(?=.[@#$%^&-+=()])(?=\\S+$).{8,20}$", message="Invalid password!")
    private String password;

    private String phone;

    private UserType userType;

    private Department department;

    private double salary;

    private Long adminId;

}
