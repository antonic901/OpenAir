package openair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.ProjectType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectBasicInformationDTO {

    private Long id;

    private String name;

    private ProjectType projectType;

    private UserBasicInformationDTO admin;

}
