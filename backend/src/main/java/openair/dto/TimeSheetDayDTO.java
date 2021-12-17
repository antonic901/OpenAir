package openair.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.utils.UnixToLocalDateConverter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TimeSheetDayDTO {

    @JsonDeserialize(using = UnixToLocalDateConverter.class)
    private LocalDate date;

    private double workTime;

    private Long taskId;

}
