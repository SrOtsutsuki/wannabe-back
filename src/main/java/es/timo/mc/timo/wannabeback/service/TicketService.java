package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.TicketDto;
import es.timo.mc.timo.wannabeback.model.entity.Ticket;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

/**
 * The interface Entrada service.
 */
public interface TicketService {


    /**
     * Gets entradas.
     *
     * @param timeOut the time out
     * @param soldOut the sold out
     * @return the entradas
     */
    List<TicketDto> getTicketsByTimeOutAndSoldOut(boolean timeOut, boolean soldOut);

    /**
     * Gets entradas no caducadas.
     *
     * @param businessId the business id
     * @return the entradas no caducadas
     */
    List<TicketDto> getTicketsActiveByBusinessId(Long businessId);


    /**
     * Gets tickets.
     *
     * @return the tickets
     */
    List<TicketDto> getTickets();

    /**
     * Save ticket ticket dto.
     *
     * @param ticketDto the ticket dto
     * @return the ticket dto
     */
    TicketDto saveTicket(TicketDto ticketDto);

    /**
     * Gets ticket by id.
     *
     * @param ticketId the ticket id
     * @return the ticket by id
     */
    TicketDto getTicketById(Long ticketId);

    /**
     * Edit ticket ticket dto.
     *
     * @param ticket the ticket
     * @return the ticket dto
     * @throws EntityNotFoundException the entity not found exception
     */
    TicketDto editTicket(TicketDto ticket) throws EntityNotFoundException;

    /**
     * Delete tickets.
     *
     * @param ids the ids
     */
    void deleteTickets(List<Long> ids);

    /**
     * Gets tickets from date.
     *
     * @param date      the date
     * @param roleStaff
     * @return the tickets from date
     */
    List<TicketDto> getTicketsFromDate(Date date, String roleStaff);

    /**
     * Gets all by id.
     *
     * @param ids the ids
     * @return the all by id
     */
    List<Ticket> getAllById(List<Long> ids);
}
