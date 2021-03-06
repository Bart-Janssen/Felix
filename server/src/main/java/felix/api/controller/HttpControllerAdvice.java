package felix.api.controller;

import felix.api.exceptions.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.persistence.EntityNotFoundException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpControllerAdvice
{
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Void> handleMissingRequestHeaderException(final MissingRequestHeaderException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<Void> handleNoSuchAlgorithmException(final NoSuchAlgorithmException e)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(AlreadyFriendsException.class)
    public ResponseEntity<Void> handleAlreadyFriendsException(final AlreadyFriendsException e)
    {
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
    }

    @ExceptionHandler(AlreadyPendingInviteException.class)
    public ResponseEntity<Void> handleAlreadyPendingInviteException(final AlreadyPendingInviteException e)
    {
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleDataIntegrityViolationException(final DataIntegrityViolationException e)
    {
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFoundException(final EntityNotFoundException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Void> handleBadRequestException(final BadRequestException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<Void> handleNotAuthorizedException(final NotAuthorizedException e)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Void> handleUnsupportedOperationException(final UnsupportedOperationException e)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Void> handleNullPointerException(final NullPointerException e)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}