package es.timo.mc.timo.wannabeback.model.dto.request;

import es.timo.mc.timo.wannabeback.model.dto.TicketDto;
import lombok.Data;

/**
 * The type Ticket request.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
public class TicketRequest {

    /**
     * The Ticket.
     */
    TicketDto ticket = null;

}
