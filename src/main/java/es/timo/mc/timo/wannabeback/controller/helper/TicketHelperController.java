package es.timo.mc.timo.wannabeback.controller.helper;

import es.timo.mc.timo.wannabeback.configuration.JwtProperties;
import es.timo.mc.timo.wannabeback.model.dto.TicketDto;
import es.timo.mc.timo.wannabeback.model.dto.request.TicketRequest;
import es.timo.mc.timo.wannabeback.service.TicketService;
import es.timo.mc.timo.wannabeback.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

/**
 * The type Ticket helper controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class TicketHelperController {

    /**
     * The Ticket service.
     */
    private final TicketService ticketService;

    /**
     * The Jwt config.
     */
    private final JwtProperties jwtConfig;

    /**
     * Gets tickets by time out and sold out.
     *
     * @param timeOut the time out
     * @param soldOut the sold out
     * @return the tickets by time out and sold out
     */
    public List<TicketDto> getTicketsByTimeOutAndSoldOut(boolean timeOut, boolean soldOut) {
        return ticketService.getTicketsByTimeOutAndSoldOut(timeOut, soldOut);
    }

    /**
     * Gets tickets active by bussines id.
     *
     * @param businessId the business id
     * @return the tickets active by bussines id
     */
    public List<TicketDto> getTicketsActiveByBussinesId(Long businessId) {
        return ticketService.getTicketsActiveByBusinessId(businessId);
    }

    /**
     * Gets tickets.
     *
     * @return the tickets
     */
    public List<TicketDto> getTickets() {
        return ticketService.getTickets();
    }

    /**
     * Save ticket.
     *
     * @param ticketRequest the ticket request
     * @return the ticket dto
     */
    public TicketDto saveTicket(TicketRequest ticketRequest) {
        return ticketService.saveTicket(ticketRequest.getTicket());
    }

    /**
     * Edit ticket ticket dto.
     *
     * @param ticketRequest the ticket request
     * @return the ticket dto
     * @throws EntityNotFoundException the entity not found exception
     */
    public TicketDto editTicket(TicketRequest ticketRequest) throws EntityNotFoundException {
        return ticketService.editTicket(ticketRequest.getTicket());
    }

    /**
     * Delete tickets.
     *
     * @param ids the ids
     */
    public void deleteTickets(List<Long> ids) {
        ticketService.deleteTickets(ids);
    }

    /**
     * Gets tickets from date.
     *
     * @param date the date
     * @param jwt  the jwt
     * @return the tickets from date
     */
    public List<TicketDto> getTicketsFromDate(Date date, String jwt) {
        JwtUtils jwtUtils = new JwtUtils(jwt, jwtConfig);
        String roleStaff = jwtUtils.obtainRoleStaff();
        return ticketService.getTicketsFromDate(date, roleStaff);
    }
}
