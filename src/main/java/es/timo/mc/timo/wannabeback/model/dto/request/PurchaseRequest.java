package es.timo.mc.timo.wannabeback.model.dto.request;

import es.timo.mc.timo.wannabeback.model.dto.PersonDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The type Reserve request.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequest {

    /**
     * The Ticket id.
     */
    @NotNull
    private Long ticketId;

    /**
     * The Discount code.
     */
    private Long couponId;

    /**
     * The Owner.
     */
    @NotNull
    @Valid
    private PersonDto owner;

    /**
     * The Friends.
     */
    @Valid
    private List<PersonDto> friends;

}
