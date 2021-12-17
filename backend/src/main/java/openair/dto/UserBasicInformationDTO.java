package openair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.UserType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserBasicInformationDTO {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String username;

    private String phone;

    private UserType userType;

}
