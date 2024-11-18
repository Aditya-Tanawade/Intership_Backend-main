package E_Doctor.internship_Project.Advices;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Input Validation Failed")
                .subErrors(errorMessage)
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> anyException(Exception ex) {
//
//        ApiError apiError = ApiError.builder()
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .message("An unexpected error occurred")
//                .build();
//
//        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
//    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorMessage = "A database integrity violation occurred.";

        // Check if the exception message contains 'email' and 'already exists'
        if (ex.getMessage().contains("email") && ex.getMessage().contains("already exists")) {
            errorMessage = "Email Already Exists! Please DO Login.";
        }

        // Build the ApiError object with the error message
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(errorMessage)
                .build();

        // Return the error response with BAD_REQUEST status
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }





}
