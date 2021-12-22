package openair.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("timesheetdays")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeSheetDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private double workTime;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @NotNull(message = "Employee can not be null")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @NotNull(message = "Task can not be null")
    private Task task;

}