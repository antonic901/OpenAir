package openair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.Employee;
import openair.model.Project;
import openair.model.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExspenseReportDTO {

    private String trackingNumber;
    private String name;
    private double refund;
    //DATUM SE GENERISE U SERVISU PRILIKOM KREIRANJA
    //private LocalDate dateOfCreation;
    private String document;
    private String description;
    private Status status;
    private Long projectId;
}
