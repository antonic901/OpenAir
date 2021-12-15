package openair.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public final ResponseEntity<List<String>> handleException(Exception ex, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();

        if(ex instanceof NotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            NotFoundException exception = (NotFoundException) ex;
            return handleNotFoundException(exception, headers, status, request);
        }
        else if(ex instanceof PeriodConflictException) {
            HttpStatus status = HttpStatus.CONFLICT;
            PeriodConflictException exception = (PeriodConflictException) ex;
            return handlePeriodConflictException(exception, headers, status, request);
        }
        else if(ex instanceof ResourceConflictException) {
            HttpStatus status = HttpStatus.CONFLICT;
            ResourceConflictException exception = (ResourceConflictException) ex;
            return handleResourceConflictException(exception, headers, status, request);
        }
        else  {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return  handleExceptionInternal(ex, null, headers, status, request);
        }
    }

    protected ResponseEntity<List<String>> handleNotFoundException(NotFoundException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(exception.getMessage());
        return handleExceptionInternal(exception, errors, headers, status, request);
    }
    protected ResponseEntity<List<String>> handlePeriodConflictException(PeriodConflictException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(exception.getMessage());
        return handleExceptionInternal(exception, errors, headers, status, request);
    }
    protected ResponseEntity<List<String>> handleResourceConflictException(ResourceConflictException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(exception.getMessage());
        return handleExceptionInternal(exception, errors, headers, status, request);
    }

    protected ResponseEntity<List<String>> handleExceptionInternal(Exception exception, List<String> errors, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if(HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, exception, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<List<String>>(errors,headers, status);
    }
}
