package openair.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.Department;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id")
    )
    private List<Project> projects = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="employee", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="employee", cascade = CascadeType.ALL)
    private List<Absence> absences = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="employee", cascade = CascadeType.ALL)
    private List<TimeSheetDay> timeSheetDays = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="employee", cascade = CascadeType.ALL)
    private List<ExpenseReport> expenseReports = new ArrayList<>();
}
