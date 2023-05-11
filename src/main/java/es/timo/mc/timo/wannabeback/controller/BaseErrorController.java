package es.timo.mc.timo.wannabeback.controller;

import es.timo.mc.timo.wannabeback.model.dto.response.ErrorResponse;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Objects;

/**
 * The type Base error controller.
 */
@Slf4j(topic = "BaseController")
@RestControllerAdvice
public class BaseErrorController extends ResponseEntityExceptionHandler {

    /**
     * The constant TRACE.
     */
    public static final String TRACE = "trace";

    /**
     * The Print stack trace.
     */
    @Value("${reflectoring.trace:false}")
    private boolean printStackTrace;

    /**
     * Handle method argument not valid response entity.
     *
     * @param ex      the ex
     * @param headers the headers
     * @param status  the status
     * @param request the request
     * @return the response entity
     */
    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Error de validación. Revisa el campo 'erros' para mas detalles");
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    /**
     * Handle all uncaught exception response entity.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception exception, WebRequest request) {
        log.error("Internal Server error", exception);
        return buildErrorResponse(exception, "Internal Server error", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handle entity not found exception response entity.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        log.error("Entity not found", exception);
        return buildErrorResponse(exception, exception.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> badCredentialsException(BadCredentialsException exception, WebRequest request) {
        log.error("Usuario no encontrado not found", exception);
        return buildErrorResponse(exception, exception.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(WannabeBackException.class)
    public ResponseEntity<ErrorResponse> handleWannabeBackException(WannabeBackException exception, WebRequest request) {
        log.error("WannabeBackException:", exception);
        return buildErrorResponse(exception, exception.getMessage(), exception.getHttpStatus(), request);
    }

    /**
     * Build error response response entity.
     *
     * @param exception  the exception
     * @param httpStatus the http status
     * @param request    the request
     * @return the response entity
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                httpStatus,
                request);
    }

    /**
     * Build error response response entity.
     *
     * @param exception  the exception
     * @param message    the message
     * @param httpStatus the http status
     * @param request    the request
     * @return the response entity
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                exception.getMessage()
        );

        if (printStackTrace && isTraceOn(request)) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        errorResponse.setError(exception.getClass().getSimpleName());
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(((ServletWebRequest) request).getRequest().getServletPath());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * Is trace on boolean.
     *
     * @param request the request
     * @return the boolean
     */
    private boolean isTraceOn(WebRequest request) {
        String[] value = request.getParameterValues(TRACE);
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }

}
