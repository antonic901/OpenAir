package openair.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.Department;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("Employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee extends User{

    @Column
    @Enumerated(EnumType.STRING)
    private Department department;

    @Column
    private double salary;

    @Column
    private Integer freeDays;

    @Column
    @FutureOrPresent
    private LocalDate dateOfHiring;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

}
