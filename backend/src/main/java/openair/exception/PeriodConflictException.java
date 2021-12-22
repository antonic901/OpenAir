package openair.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodConflictException extends  RuntimeException {

    private final Long resourceId;

    public PeriodConflictException(Long resourceId, String message) {
        super(message);
        this.resourceId = resourceId;
    }

}
