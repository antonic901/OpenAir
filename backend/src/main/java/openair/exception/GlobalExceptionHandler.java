package openair.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public final ResponseEntity<List<String>> handleNotFoundException(NotFoundException ex, WebRequest request){
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_FOUND;
        List<String> errors = Collections.singletonList(ex.getMessage());

        return new ResponseEntity<>(errors, headers, status);
    }

    @ExceptionHandler({PeriodConflictException.class})
    public final ResponseEntity<List<String>> handlePeriodConflictException(PeriodConflictException ex, WebRequest request){
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.CONFLICT;
        List<String> errors = Collections.singletonList(ex.getMessage());

        return new ResponseEntity<>(errors, headers, status);
    }

    @ExceptionHandler({ResourceConflictException.class})
    public final ResponseEntity<List<String>> handleResourceConflictException(ResourceConflictException ex, WebRequest request){
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.CONFLICT;
        List<String> errors = Collections.singletonList(ex.getMessage());

        return new ResponseEntity<>(errors, headers, status);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public final ResponseEntity<List<String>> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request){
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(errors, headers, status);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public final ResponseEntity<List<String>> handleException(ConstraintViolationException ex, WebRequest request){

        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = Collections.singletonList(ex.getMessage());
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);

        return new ResponseEntity<>(errors, headers, status);
    }

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<List<String>> handleException(Exception ex, WebRequest request){

        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        List<String> errors = Collections.singletonList(ex.getMessage());
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);

        return new ResponseEntity<>(errors, headers, status);
    }
}
