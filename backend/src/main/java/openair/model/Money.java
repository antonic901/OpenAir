package openair.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.model.enums.Currency;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
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
    @NotNull(message = "Quantity cannot be null")
    private double quantity;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Currency cannot be null")
    private Currency currency;

    @Column
    @PastOrPresent
    @NotNull(message = "Date cannot be null")
    private LocalDate date;

}
