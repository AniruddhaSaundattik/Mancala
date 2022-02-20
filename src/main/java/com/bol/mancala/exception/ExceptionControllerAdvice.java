package com.bol.mancala.exception;

import com.bol.mancala.model.exception.ValidationErrorResponse;
import com.bol.mancala.model.exception.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();
        for(FieldError fieldError: exception.getBindingResult().getFieldErrors()){
            logger.error("Error {} with message {}", fieldError.getField(), fieldError.getDefaultMessage());
            validationErrorResponse.getViolations().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage())
            );
        }
        return validationErrorResponse;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity onBadCredentialsException(BadCredentialsException exception) {
        logger.error("Error getting JWT token");
        return new ResponseEntity("Error getting JWT token", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MancalaException.class)
    public ResponseEntity onMancalaException(MancalaException exception) {
        logger.error("Catching any exception in application");
        return new ResponseEntity("Something is not right", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
