package com.sarahemmati.wallet.common;


import com.sarahemmati.wallet.api.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse err(String code, String msg, HttpServletRequest req) {
        String rid = req.getHeader("X-Request-Id");
        return new ErrorResponse(code, msg, Instant.now(), req.getRequestURI(), rid);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + (fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(err("VALIDATION_ERROR", msg, req));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadReq(IllegalArgumentException ex, HttpServletRequest req) {
        String code = switch (ex.getMessage()) {
            case "AMOUNT_INVALID" -> "AMOUNT_INVALID";
            case "WALLET_NOT_FOUND" -> "WALLET_NOT_FOUND";
            case "USERNAME_TAKEN" -> "USERNAME_TAKEN";
            default -> "BAD_REQUEST";
        };
        return ResponseEntity.badRequest().body(err(code, ex.getMessage(), req));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleDenied(AccessDeniedException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err("FORBIDDEN", "Access denied", req));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err("DATA_INTEGRITY", "Conflict with data constraints", req));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAny(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(err("INTERNAL_ERROR", "Unexpected error", req));
    }
}
