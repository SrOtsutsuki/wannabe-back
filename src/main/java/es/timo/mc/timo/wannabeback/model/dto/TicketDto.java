package es.timo.mc.timo.wannabeback.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.timo.mc.timo.wannabeback.model.entity.Business;
import lombok.*;

import java.util.Date;
import java.util.Objects;

/**
 * The type Entrada dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {

    /**
     * The Id.
     */
    private Long id;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Description.
     */
    private String description;

    /**
     * The Short description.
     */
    private String shortDescription;
    /**
     * The Main pic.
     */
    private ImageDto mainPic;

    /**
     * The Ticket pic.
     */
    private ImageDto ticketPic;

    /**
     * The Reserve pic.
     */
    private ImageDto reservePic;

    /**
     * The Capacity.
     */
    private Integer capacity;

    /**
     * The Capacity reserve.
     */
    private Integer capacityReserve;

    /**
     * The Remaining capacity.
     */
    @Getter
    private Integer remainingCapacity;

    /**
     * The Price.
     */
    private Double price;

    /**
     * The Active.
     */
    @Getter
    private Boolean active;

    /**
     * The Date.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "CET")
    private Date date;

    /**
     * The End sale date.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "CET")
    private Date endSaleDate;

    /**
     * The Start sale date.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "CET")
    private Date startSaleDate;

    /**
     * The Closing date.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "CET")
    private Date closingDate;


    /**
     * The Business.
     */
    private Business business;

    /**
     * The Sold out.
     */
    @Getter
    private Boolean soldOut;

    /**
     * The Time out.
     */
    @Getter
    private Boolean timeOut;


    /**
     * Get sold out boolean.
     *
     * @return the boolean
     */
    public Boolean getSoldOut() {
        if (Objects.equals(this.capacityReserve, this.capacity)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }


    /**
     * Get remaining capacity integer.
     *
     * @return the integer
     */
    public Integer getRemainingCapacity() {
        return (capacity - capacityReserve);
    }

    /**
     * Get active boolean.
     *
     * @return the boolean
     */
    public Boolean getActive() {
        Date now = new Date();
        if ((now.after(startSaleDate) || now.equals(startSaleDate)) && now.before(closingDate)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Get time out boolean.
     *
     * @return the boolean
     */
    public Boolean getTimeOut() {
        if (new Date().after(endSaleDate)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
