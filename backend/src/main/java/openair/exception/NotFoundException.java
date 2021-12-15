package openair.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends RuntimeException {

    private Long resourceId;

    public NotFoundException(String message) { super(message); }

    public NotFoundException(Long resourceId, String message) {
        super(message);
        this.setResourceId(resourceId);
    }

}