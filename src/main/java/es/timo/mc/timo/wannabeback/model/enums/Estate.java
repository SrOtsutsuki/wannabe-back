package es.timo.mc.timo.wannabeback.model.enums;

import lombok.Getter;

/**
 * The enum Estate.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Getter
public enum Estate {

    /**
     * Pending estate.
     */
    PENDING("PENDIENTE"),
    /**
     * Paid estate.
     */
    PAID("PAGADO");

    /**
     * The Value.
     */
    private final String value;

    /**
     * Instantiates a new Estate.
     *
     * @param value the value
     */
    Estate(String value){
        this.value = value;
    }
}

