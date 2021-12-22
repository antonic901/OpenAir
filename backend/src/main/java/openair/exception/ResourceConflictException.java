package openair.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceConflictException extends RuntimeException {

    private Long resourceId;

    public ResourceConflictException(String message){
        super(message);
    }

    public ResourceConflictException(Long resourceId, String message) {
        super(message);
        this.resourceId = resourceId;
    }
}
