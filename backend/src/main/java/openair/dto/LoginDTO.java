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
public class LoginDTO {

    private String accessToken;

    private Long expiresIn;

    private UserType userType;

    private Long userId;

}

