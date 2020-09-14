package felix.api.controller;

import felix.api.exceptions.BadRequestException;
import felix.api.exceptions.ItemNotFoundException;
import felix.api.exceptions.NotAuthorizedException;
import felix.api.service.event.IEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import felix.api.models.Event;
import felix.api.models.EventType;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpControllerAdvice
{
    private final IEventService eventService;

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity handleItemNotFoundException(final ItemNotFoundException e)
    {
        this.createEvent(e, EventType.INFO);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestException(final BadRequestException e)
    {
        this.createEvent(e, EventType.ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity handleNotAuthorizedException(final NotAuthorizedException e)
    {
        this.createEvent(e, EventType.WARNING);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity handleUnsupportedOperationException(final UnsupportedOperationException e)
    {
        this.createEvent(e, EventType.CRITICAL);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private void createEvent(Exception e, EventType level)
    {
        Event event = new Event(e.getClass() + " At " + (e.getMessage() == null ? "" : ("[" + e.getMessage() + "] ")) + e.getStackTrace()[0].getClassName(), level);
        new Thread(() ->
        {
            try
            {
                eventService.addNew(event);
            }
            catch (Exception ex)
            {
                log.error("Cannot log event!");
            }
        }).start();
    }
}