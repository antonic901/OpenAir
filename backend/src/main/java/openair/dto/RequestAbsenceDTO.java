package openair.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.utils.UnixToLocalDateTimeConverter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestAbsenceDTO {

    @JsonDeserialize(using = UnixToLocalDateTimeConverter.class)
    private LocalDateTime startTime;

    @JsonDeserialize(using = UnixToLocalDateTimeConverter.class)
    private LocalDateTime endTime;

}
