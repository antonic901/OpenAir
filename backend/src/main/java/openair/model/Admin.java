package openair.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Admin")
//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
public class Admin extends User {

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="admin", cascade = CascadeType.ALL)
    private List<Employee> employeeList = new ArrayList<Employee>();

}
