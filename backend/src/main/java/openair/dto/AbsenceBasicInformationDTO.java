package openair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.Period;
import openair.model.enums.Status;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AbsenceBasicInformationDTO {

    private Long id;

    private Period period;

    private Status status;

    private UserBasicInformationDTO employee;

    private UserBasicInformationDTO admin;

}
