package es.timo.mc.timo.wannabeback.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.timo.mc.timo.wannabeback.model.enums.Estate;
import lombok.*;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * The type Order dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseDto {

    /**
     * The Id.
     */
    private Long id;

   
    /**
     * The Order id.
     */
    private String orderId;

    /**
     * The Buyer.
     */
    private PersonDto buyer;

    /**
     * The Reserves.
     */
    private List<ReserveDto> reserves;

    /**
     * The Ticket.
     */
    private TicketDto ticket;

    /**
     * The Coupon.
     */
    private CouponDto coupon;

    /**
     * The Date.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "CET")
    private Date date = new Date();

    /**
     * The Purchase reference.
     */
    private String purchaseReference;

    /**
     * The Estate.
     */
    private Estate estate = Estate.PENDING;

    /**
     * The Total.
     */
    @Getter
    private Double total;

    /**
     * Format total string.
     *
     * @return the string
     */
    public String formatTotal() {
        double number = total;
        DecimalFormat df = new DecimalFormat("0000.##");
        return df.format(number * 100);
    }

}
