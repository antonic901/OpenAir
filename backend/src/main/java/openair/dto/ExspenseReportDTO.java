package openair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.Money;
import openair.model.enums.Status;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExspenseReportDTO {

    private String trackingNumber;
    private String name;
    private Money refund;
    private String document;
    private String description;
    private Status status;
    private Long projectId;
}
