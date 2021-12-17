package openair.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.ProjectType;
import javax.persistence.Column;
import javax.persistence.Entity;

public interface PdfTableDataOutput {
    String getEmployee();
    String getProject();
    double getWork_time();
    String getProject_type();
}