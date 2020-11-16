package felix.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@Order
public class HttpFallBackControllerAdvise
{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(final Exception e)
    {
        log.error(String.format("Fallback exception handled; %s at: %s", e.getMessage(), e.getStackTrace()[0]));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleRuntimeException(final RuntimeException e)
    {
        log.error(String.format("Fallback exception handled; %s at: %s", e.getMessage(), e.getStackTrace()[0]));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}