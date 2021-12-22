package openair.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "absences")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "period", nullable = false)
    @Embedded
    private Period period;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @NotNull(message = "Employee can not be null")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    @NotNull(message = "Admin can not be null")
    private Admin admin;

}
