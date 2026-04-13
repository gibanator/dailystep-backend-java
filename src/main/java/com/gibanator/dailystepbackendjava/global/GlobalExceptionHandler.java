package com.gibanator.dailystepbackendjava.global;

import com.gibanator.dailystepbackendjava.auth.exceptions.EmailAlreadyExistsException;
import com.gibanator.dailystepbackendjava.auth.exceptions.InvalidCredentialsException;
import com.gibanator.dailystepbackendjava.category.exception.CategoryNotFoundException;
import com.gibanator.dailystepbackendjava.global.dto.ErrorResponse;
import com.gibanator.dailystepbackendjava.user.exception.UserNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String msg = ex
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed.");

        ErrorResponse resp = new ErrorResponse("Validation failed.", msg);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(EmailAlreadyExistsException ex) {

            ErrorResponse resp = new ErrorResponse("Data integrity violation.", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(resp);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {

        ErrorResponse resp = new ErrorResponse("Not found.", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(resp);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFound(CategoryNotFoundException ex) {
        ErrorResponse resp = new ErrorResponse("Not found.", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(resp);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorResponse resp = new ErrorResponse("Invalid credentials.", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(resp);
    }
}
