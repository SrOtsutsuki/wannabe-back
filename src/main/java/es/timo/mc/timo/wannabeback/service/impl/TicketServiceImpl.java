package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.model.dto.TicketDto;
import es.timo.mc.timo.wannabeback.model.entity.Image;
import es.timo.mc.timo.wannabeback.model.entity.Ticket;
import es.timo.mc.timo.wannabeback.model.enums.RoleBusiness;
import es.timo.mc.timo.wannabeback.repository.TicketRepository;
import es.timo.mc.timo.wannabeback.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * The type Entrada service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    /**
     * The Entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The Ticket repository.
     */
    private final TicketRepository ticketRepository;

    /**
     * The Model mapper.
     */
    private final ModelMapper modelMapper;

    /**
     * Gets tickets by time out and sold out.
     *
     * @param timeOut the time out
     * @param soldOut the sold out
     * @return the tickets by time out and sold out
     */
    @Override
    @Transactional(readOnly = true)
    public List<TicketDto> getTicketsByTimeOutAndSoldOut(boolean timeOut, boolean soldOut) {

        log.info("Consultando Tickets con timeOut: {} y soldOut: {}", timeOut, soldOut);

        List<Ticket> tickets = ticketRepository.findActiveTickets(new Date());

        List<TicketDto> ticketDtos = tickets.stream()
                .map(entrada -> modelMapper.map(entrada, TicketDto.class))
                .filter(ticketDto -> ticketDto.getTimeOut() == timeOut)
                .filter(ticketDto -> ticketDto.getSoldOut() == soldOut)
                .toList();

        log.info("Tickets obtenidos: {}", ticketDtos.size());

        return ticketDtos;
    }

    /**
     * Gets tickets not expired by business id.
     *
     * @param businessId the business id
     * @return the tickets not expired by business id
     */
    @Override
    @Transactional(readOnly = true)
    public List<TicketDto> getTicketsActiveByBusinessId(Long businessId) {

        log.info("Consultando Tickets con id negocio: {}", businessId);

        List<Ticket> tickets = ticketRepository.findActiveTicketsByBusinessId(businessId, new Date());

        List<TicketDto> ticketDto = tickets.stream()
                .map(entrada -> modelMapper.map(entrada, TicketDto.class))
                .toList();

        return ticketDto;
    }


    /**
     * Gets tickets.
     *
     * @return the tickets
     */
    @Override
    @Transactional(readOnly = true)
    public List<TicketDto> getTickets() {
        List<Ticket> tickets = ticketRepository.findAllByOrderByDateDesc();
        return tickets.stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                .toList();
    }

    /**
     * Save ticket ticket dto.
     *
     * @param ticketDto the ticket dto
     * @return the ticket dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TicketDto saveTicket(TicketDto ticketDto) {
        log.info("Guardando el ticket: {}", ticketDto.getName());
        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
        ticket = ticketRepository.save(ticket);
        entityManager.flush();
        entityManager.refresh(ticket);
        return modelMapper.map(ticket, TicketDto.class);
    }

    /**
     * Gets ticket by id.
     *
     * @param ticketId the ticket id
     * @return the ticket by id
     */
    @Override
    @Transactional(readOnly = true)
    public TicketDto getTicketById(Long ticketId) {
        log.info("Consultando ticket por id: {}", ticketId);
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket != null) {
            return modelMapper.map(ticket, TicketDto.class);
        }
        return null;
    }

    /**
     * Edit ticket ticket dto.
     *
     * @param ticket the ticket
     * @return the ticket dto
     * @throws EntityNotFoundException the entity not found exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TicketDto editTicket(TicketDto ticket) throws EntityNotFoundException {
        log.info("Editando ticket: {}", ticket.getId());
        Ticket ticketSaved = ticketRepository.findById(ticket.getId()).orElse(null);
        if (ticketSaved != null) {
            BeanUtils.copyProperties(ticket, ticketSaved);
            ticketSaved.setTicketPic(modelMapper.map(ticket.getTicketPic(), Image.class));
            ticketSaved.setMainPic(modelMapper.map(ticket.getMainPic(), Image.class));
            ticketSaved.setReservePic(modelMapper.map(ticket.getReservePic(), Image.class));
            ticketSaved = ticketRepository.save(ticketSaved);
            entityManager.flush();
            entityManager.refresh(ticketSaved);
            return modelMapper.map(ticketSaved, TicketDto.class);
        }
        throw new EntityNotFoundException("No se ha encontrado el Evento");
    }

    /**
     * Delete tickets.
     *
     * @param ids the ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTickets(List<Long> ids) {
        log.info("Eliminando tickets: {}", ids);
        ticketRepository.deleteAllById(ids);
    }


    /**
     * Gets tickets from date.
     *
     * @param date      the date
     * @param roleStaff the role staff
     * @return the tickets from date
     */
    @Override
    @Transactional(readOnly = true)
    public List<TicketDto> getTicketsFromDate(Date date, String roleStaff) {
        log.info("Consultando tickets a partir de la fecha : {}", date);
        List<Ticket> tickets = null;
        if (roleStaff == null) {
            tickets = ticketRepository.findAllByClosingDateAfterOrderByDateAsc(date);
        } else if (roleStaff.equals(RoleBusiness.CASINO.getRoleName())) {
            tickets = ticketRepository.findAllByClosingDateAfterAndBusinessNameOrderByDateAsc(date,
                    RoleBusiness.CASINO.getBusiness().getName());
        } else if (roleStaff.equals(RoleBusiness.BRUTAL.getRoleName())) {
            tickets = ticketRepository.findAllByClosingDateAfterAndBusinessNameOrderByDateAsc(date,
                    RoleBusiness.BRUTAL.getBusiness().getName());
        }

        return tickets.stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                .toList();

    }

    /**
     * Get all by id list.
     *
     * @param ids the ids
     * @return the list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Ticket> getAllById(List<Long> ids) {
        log.info("Consultando tickets por id, ids: {}", ids);
        List<Ticket> tickets = ticketRepository.findAllById(ids);
        return tickets;
    }


}
