package openair.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.Department;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee extends User{

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Department department;

    @Column(nullable = true)
    private double salary;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = true)
    private Admin admin;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id")
    )
    private List<Project> projects = new ArrayList<Project>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="employee", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<Task>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="employee", cascade = CascadeType.ALL)
    private List<Absence> absences = new ArrayList<Absence>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="employee", cascade = CascadeType.ALL)
    private List<TimeSheetDay> timeSheetDays = new ArrayList<TimeSheetDay>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="employee", cascade = CascadeType.ALL)
    private List<ExpenseReport> expenseReports = new ArrayList<ExpenseReport>();
}
