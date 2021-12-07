package openair.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Admin")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Admin extends User {

    @OneToMany(fetch = FetchType.LAZY, mappedBy="admin", cascade = CascadeType.ALL)
    private List<Employee> employeeList = new ArrayList<Employee>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="admin", cascade = CascadeType.ALL)
    private List<Project> projects = new ArrayList<Project>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="admin", cascade = CascadeType.ALL)
    private List<Absence> absences = new ArrayList<Absence>();

}
