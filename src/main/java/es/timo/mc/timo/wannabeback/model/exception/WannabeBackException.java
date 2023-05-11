package es.timo.mc.timo.wannabeback.model.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * The type Wannabe back exception.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
public class WannabeBackException extends Exception {

    /**
     * The Http status.
     */
    private HttpStatus httpStatus;

    /**
     * Instantiates a new Wannabe back exception.
     */
    public WannabeBackException() {
    }

    /**
     * Instantiates a new Wannabe back exception.
     *
     * @param message    the message
     * @param httpStatus the http status
     */
    public WannabeBackException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
