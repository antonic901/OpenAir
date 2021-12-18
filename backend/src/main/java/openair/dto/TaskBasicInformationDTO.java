package openair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskBasicInformationDTO {

    private Long id;

    private String name;

    private ProjectBasicInformationDTO project;

    private UserBasicInformationDTO employee;

}
