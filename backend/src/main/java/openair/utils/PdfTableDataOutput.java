package openair.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.ProjectType;
import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PdfTableDataOutput {
    @Column(name = "Employee")
    private String employee_name;
    @Column(name = "Project")
    private String project_name;
    @Column(name = "Work_time")
    private double work_time;
    @Column(name = "Project_type")
    private String project_type;
}