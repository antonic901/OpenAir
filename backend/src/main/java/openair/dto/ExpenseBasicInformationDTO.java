package openair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.Status;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseBasicInformationDTO {

    private Long id;

    private String trackingNumber;

    private String name;

    private MoneyDTO refund;

    private String description;

    private String document;

    private Status status;

    private UserBasicInformationDTO employee;

    private ProjectBasicInformationDTO project;
}
