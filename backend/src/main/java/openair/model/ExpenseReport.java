package openair.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.Status;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "expensereports")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ExpenseReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String trackingNumber;

    @Column
    private String name;

    @Column
    private double refund;

    @Column
    private LocalDate dateOfCreation;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

}
