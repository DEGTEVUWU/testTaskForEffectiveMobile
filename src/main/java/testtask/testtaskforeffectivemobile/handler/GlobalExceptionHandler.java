package testtask.testtaskforeffectivemobile.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import testtask.testtaskforeffectivemobile.exeption.EmailAlreadyExistsException;
import testtask.testtaskforeffectivemobile.exeption.LastEmailContactException;
import testtask.testtaskforeffectivemobile.exeption.LastPhoneNumberContactException;
import testtask.testtaskforeffectivemobile.exeption.LoginAlreadyExistsException;
import testtask.testtaskforeffectivemobile.exeption.PhoneNumberAlreadyExistsException;
import testtask.testtaskforeffectivemobile.exeption.ResourceNotFoundException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<Object> handleEmailAlreadyExists(DataIntegrityViolationException ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
//    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<Object> handlePhoneNumberAlreadyExists(PhoneNumberAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LoginAlreadyExistsException.class)
    public ResponseEntity<Object> handleLoginAlreadyExists(LoginAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(LastEmailContactException.class)
    public ResponseEntity<Object> handleLastEmailContactException(LastEmailContactException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(LastPhoneNumberContactException.class)
    public ResponseEntity<Object> handleLastPhoneNumberContactException(LastPhoneNumberContactException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
