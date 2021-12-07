package openair.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import openair.utils.UnixToLocalDateTimeConverter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Period {

    @JsonDeserialize(using = UnixToLocalDateTimeConverter.class)
    @Column
    private LocalDateTime startTime;

    @JsonDeserialize(using = UnixToLocalDateTimeConverter.class)
    @Column
    private LocalDateTime endTime;

}
