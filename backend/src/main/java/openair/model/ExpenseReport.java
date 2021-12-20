package openair.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.Status;
import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull(message = "Tracking number cannot be null")
    @Size(min = 1, max = 30, message
            = "Name must be between 1 and 30 characters")
    private String trackingNumber;

    @Column
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 30, message
            = "Name must be between 1 and 30 characters")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "money_id", referencedColumnName = "id", nullable = false)
    private Money refund;

    @Column
    @FutureOrPresent
    @NotNull(message = "Date of creation cannot be null")
    private LocalDate dateOfCreation;

    @Column
    @NotNull(message = "Description cannot be null")
    @Size(min = 1, max = 100, message
            = "Name must be between 1 and 100 characters")
    private String description;

    @Column
    @NotNull(message = "Document cannot be null")
    @Size(min = 1, max = 50, message
            = "Name must be between 1 and 50 characters")
    private String document;

    @Column
    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

}
