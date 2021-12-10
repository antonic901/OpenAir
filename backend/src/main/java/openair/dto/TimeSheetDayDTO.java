package openair.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.utils.UnixToLocalDateTimeConverter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TimeSheetDayDTO {

    @JsonDeserialize(using = UnixToLocalDateTimeConverter.class)
    private LocalDateTime date;

    private double workTime;

    private Long taskId;

}
