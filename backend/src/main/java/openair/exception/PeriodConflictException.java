package openair.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodConflictException extends  RuntimeException {

    private Long resourceId;

    public PeriodConflictException(Long resourceId, String message) {
        super(message);
        this.setResourceId(resourceId);
    }

}
