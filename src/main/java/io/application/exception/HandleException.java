package io.application.exception;

import io.application.domain.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.application.util.DateUtil.dateTimeFormatter;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandleException.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        LOGGER.error(ex.getMessage());
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        String message = errors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>(new HttpResponse()
                .setReason("Invalid fields: " + message)
                .setDeveloperMessage(ex.getMessage())
                .setStatus(status)
                .setStatusCode(status.value())
                .setTimestamp(LocalDateTime.now().format(dateTimeFormatter())),
                status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             @Nullable Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        LOGGER.error(ex.getMessage());
        return new ResponseEntity<>(new HttpResponse()
                .setReason(ex.getMessage())
                .setDeveloperMessage(ex.getMessage())
                .setStatus(status)
                .setStatusCode(status.value())
                .setTimestamp(LocalDateTime.now().format(dateTimeFormatter())),
                status);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<HttpResponse<?>> illegalStateException(IllegalStateException ex) {
        return createHttpErrorResponse(BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(NoteNotFoundException.class)
    protected ResponseEntity<HttpResponse<?>> notFoundException(NoteNotFoundException ex) {
        return createHttpErrorResponse(BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(NoResultException.class)
    protected ResponseEntity<HttpResponse<?>> noResultException(NoResultException ex) {
        return createHttpErrorResponse(BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(ServletException.class)
    protected ResponseEntity<HttpResponse<?>> servletException(ServletException ex) {
        return createHttpErrorResponse(BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<HttpResponse<?>> exception(Exception ex) {
        return createHttpErrorResponse(BAD_REQUEST, ex.getMessage(), ex);
    }

    private ResponseEntity<HttpResponse<?>> createHttpErrorResponse(HttpStatus status, String reason, Exception ex) {
        LOGGER.error(ex.getMessage());
        return new ResponseEntity<>(new HttpResponse()
                .setReason(reason)
                .setDeveloperMessage(ex.getMessage())
                .setStatus(status)
                .setStatusCode(status.value())
                .setTimestamp(LocalDateTime.now().format(dateTimeFormatter())), status);
    }
}
