package openair.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.Currency;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "money")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Money {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double quantity;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column
    private LocalDate date;

    @JsonIgnore
    @OneToOne(mappedBy = "refund")
    private ExpenseReport expenseReport;

}
