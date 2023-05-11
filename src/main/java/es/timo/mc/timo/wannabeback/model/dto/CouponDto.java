package es.timo.mc.timo.wannabeback.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * The type Coupon dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponDto {


    /**
     * The Id.
     */
    private Long id;

    /**
     * The Code.
     */
    private String code;

    /**
     * The Valid since.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "CET")
    private Date validSince;

    /**
     * The Valid until.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "CET")
    private Date validUntil;

    /**
     * The Discount.
     */
    private Double discount;

    /**
     * The Max applications.
     */
    private Integer maxApplications;

    /**
     * The Total people per use.
     */
    private Integer totalPeoplePerUse;

    /**
     * The Capacity reserve.
     */
    private Integer applications;

    /**
     * The Consummation.
     */
    private Boolean consummation;


    /**
     * The Tickets.
     */
    private List<TicketDto> tickets;

    /**
     * The Created date.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "CET")
    private Date createdDate;

    /**
     * Is valid boolean.
     *
     * @return the boolean
     */
    public Boolean isValid() {
        if ((new Date().after(validSince) && new Date().before(validUntil)) && applications < maxApplications) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
