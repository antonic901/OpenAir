package openair.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.utils.UnixToLocalDateTimeConverter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Period {

    @JsonDeserialize(using = UnixToLocalDateTimeConverter.class)
    @Future
    @NotNull(message = "Start time cannot be null")
    private LocalDateTime startTime;

    @JsonDeserialize(using = UnixToLocalDateTimeConverter.class)
    @Future
    @NotNull(message = "End time cannot be null")
    private LocalDateTime endTime;

}
