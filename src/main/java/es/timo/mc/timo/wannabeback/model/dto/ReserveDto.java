package es.timo.mc.timo.wannabeback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Reserve dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveDto {

    /**
     * The Id.
     */
    private Long id;

    /**
     * The Reserve code.
     */
    private String reserveCode;

    /**
     * The Order.
     */
    private PurchaseDto purchase;

    /**
     * The Person.
     */
    private PersonDto person;

    /**
     * The Used.
     */
    private Boolean used = Boolean.FALSE;

    /**
     * The Consummation.
     */
    private Boolean consummation = Boolean.TRUE;

    /**
     * The Canceled.
     */
    private Boolean canceled = Boolean.FALSE;

    /**
     * Instantiates a new Reserve dto.
     *
     * @param person the person
     */
    public ReserveDto(PersonDto person) {
        this.person = person;
    }

    /**
     * Has consumation boolean.
     *
     * @return the boolean
     */
    public Boolean getConsummation() {
        if (purchase != null && purchase.getCoupon() != null) {
            return purchase.getCoupon().getConsummation();
        }
        return Boolean.TRUE;
    }
}
