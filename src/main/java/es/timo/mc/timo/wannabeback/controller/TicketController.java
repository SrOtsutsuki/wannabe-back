package es.timo.mc.timo.wannabeback.controller;

import es.timo.mc.timo.wannabeback.controller.helper.TicketHelperController;
import es.timo.mc.timo.wannabeback.model.dto.TicketDto;
import es.timo.mc.timo.wannabeback.model.dto.request.TicketRequest;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The type Entradas controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Controller
@RequestMapping("/api/v1/ticket")
@Api(tags = "Ticket")
@Log4j2
@RequiredArgsConstructor
public class TicketController extends BaseErrorController {


    /**
     * The Ticket helper controller.
     */
    private final TicketHelperController ticketHelperController;

    /**
     * Obtener entradas response entity.
     *
     * @param timeOut the time out
     * @param soldOut the sold out
     * @return the response entity
     */
    @GetMapping("/getTicketsByTimeOutAndSoldOut")
    public ResponseEntity<List<TicketDto>> getTicketsByTimeOutAndSoldOut(
            @RequestParam(defaultValue = "false", required = false) boolean timeOut,
            @RequestParam(defaultValue = "false", required = false) boolean soldOut) {
        List<TicketDto> ticketDtoList = ticketHelperController.getTicketsByTimeOutAndSoldOut(timeOut, soldOut);
        return ResponseEntity.ok(ticketDtoList);
    }

    /**
     * Obtener entradas no caducadas response entity.
     *
     * @param businessId the business id
     * @return the response entity
     */
    @GetMapping("/getTicketsActiveByBussinesId")
    public ResponseEntity<List<TicketDto>> getTicketsActiveByBussinesId(
            @RequestParam(required = true) Long businessId) {
        List<TicketDto> ticketDtoList = ticketHelperController.getTicketsActiveByBussinesId(businessId);
        return ResponseEntity.ok(ticketDtoList);
    }


    /**
     * Get tickets response entity.
     *
     * @return the response entity
     */
    @GetMapping("/getTickets")
    public ResponseEntity<List<TicketDto>> getTickets() {
        List<TicketDto> ticketDtoList = ticketHelperController.getTickets();
        return ResponseEntity.ok(ticketDtoList);
    }

    /**
     * Save ticket response entity.
     *
     * @param ticketRequest the ticket request
     * @return the response entity
     */
    @PostMapping("/saveTicket")
    public ResponseEntity<TicketDto> saveTicket(@RequestBody TicketRequest ticketRequest) {
        TicketDto ticketDto = ticketHelperController.saveTicket(ticketRequest);
        return new ResponseEntity<>(ticketDto, HttpStatus.CREATED);
    }

    /**
     * Edit ticket response entity.
     *
     * @param ticketRequest the ticket request
     * @return the response entity
     * @throws EntityNotFoundException the entity not found exception
     */
    @PutMapping("/editTicket")
    public ResponseEntity<TicketDto> editTicket(@RequestBody TicketRequest ticketRequest) throws EntityNotFoundException {
        TicketDto ticketDto = ticketHelperController.editTicket(ticketRequest);
        return ResponseEntity.ok(ticketDto);
    }

    /**
     * Delete tickets response entity.
     *
     * @param ids the ids
     * @return the response entity
     */
    @DeleteMapping("deleteTickets")
    public ResponseEntity<?> deleteTickets(@RequestBody List<Long> ids) {
        ticketHelperController.deleteTickets(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * Gets tickets from date.
     *
     * @param date    the date
     * @param request the request
     * @return the tickets from date
     * @throws ParseException the parse exception
     */
    @GetMapping("getTicketsFromDate")
    public ResponseEntity<List<TicketDto>> getTicketsFromDate(@RequestParam String date, HttpServletRequest request) throws ParseException {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        Date dateFormated = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date);
        List<TicketDto> tickets = ticketHelperController.getTicketsFromDate(dateFormated, jwt);
        return ResponseEntity.ok(tickets);
    }
}
