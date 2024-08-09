//package com.example.job_portal_api.config;
//
//import com.example.job_portal_api.exceptions.EmailAlreadyUsedException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(EmailAlreadyUsedException.class)
//    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex) {
//        ApiErrorResponse response = new ApiErrorResponse(
//                HttpStatus.CONFLICT.value(),
//                ex.getMessage(),
//                System.currentTimeMillis()
//        );
//        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//    }
//
//
//}
//
