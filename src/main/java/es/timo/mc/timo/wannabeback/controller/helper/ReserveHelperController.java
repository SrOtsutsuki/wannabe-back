package es.timo.mc.timo.wannabeback.controller.helper;

import com.lowagie.text.DocumentException;
import es.timo.mc.timo.wannabeback.configuration.JwtProperties;
import es.timo.mc.timo.wannabeback.model.dto.ReserveDto;
import es.timo.mc.timo.wannabeback.model.dto.ReserveDtoWithQrCode;
import es.timo.mc.timo.wannabeback.model.dto.ReserveListDto;
import es.timo.mc.timo.wannabeback.model.dto.TicketDto;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import es.timo.mc.timo.wannabeback.service.ComunicacionesService;
import es.timo.mc.timo.wannabeback.service.PdfExporterService;
import es.timo.mc.timo.wannabeback.service.ReserveService;
import es.timo.mc.timo.wannabeback.service.TicketService;
import es.timo.mc.timo.wannabeback.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The type Reserve helper controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ReserveHelperController {

    /**
     * The Reserve service.
     */
    private final ReserveService reserveService;

    /**
     * The Ticket service.
     */
    private final TicketService ticketService;

    /**
     * The Exporter.
     */
    private final PdfExporterService exporter;

    /**
     * The Jwt config.
     */
    private final JwtProperties jwtConfig;

    /**
     * The Comunicaciones service.
     */
    private final ComunicacionesService comunicacionesService;

    /**
     * The Model mapper.
     */
    private final ModelMapper modelMapper;

    /**
     * Gets reserve by reserve code.
     *
     * @param reserveCode the reserve code
     * @return the reserve by reserve code
     * @throws WannabeBackException the wannabe back exception
     */
    public ReserveDto getReserveByReserveCode(String reserveCode) throws WannabeBackException {
        ReserveDto reserveDto = reserveService.getReserveByReserveCode(reserveCode);
        cleanData(reserveDto);
        return reserveDto;
    }

    /**
     * Toogle used from reserve id reserve dto.
     *
     * @param reserveId the reserve id
     * @return the reserve dto
     */
    public ReserveDto toogleUsedFromReserveId(Long reserveId) {
        ReserveDto reserveDto = reserveService.toogleUsedFromReserveId(reserveId);
        cleanData(reserveDto);
        return reserveDto;
    }

    /**
     * Gets reserves by ticket id.
     *
     * @param ticketId the ticket id
     * @param jwt      the jwt
     * @return the reserves by ticket id
     */
    public List<ReserveDto> getReservesByTicketId(Long ticketId, String jwt) {

        JwtUtils jwtUtils = new JwtUtils(jwt, jwtConfig);
        String roleStaff = jwtUtils.obtainRoleStaff();

        List<ReserveDto> reserveDtoList = reserveService.getReservesByTicketId(ticketId, roleStaff);
        reserveDtoList.forEach(reserveDto -> {
            reserveDto.getPurchase().setTicket(null);
            reserveDto.getPurchase().setReserves(null);
            if (reserveDto.getPurchase().getCoupon() != null) {
                reserveDto.getPurchase().getCoupon().setTickets(null);
            }
            reserveDto.getPurchase().setBuyer(null);
        });
        return reserveDtoList;
    }

    /**
     * Clean data.
     *
     * @param reserveDto the reserve dto
     */
    private void cleanData(ReserveDto reserveDto) {
        reserveDto.setPurchase(null);
    }

    /**
     * Generate pdf with reseres by ticketid.
     *
     * @param response the response
     * @param ticketId the ticket id
     * @throws DocumentException the document exception
     * @throws IOException       the io exception
     */
    public void generatePdfWithReseresByTicketId(HttpServletResponse response, Long ticketId) throws DocumentException, IOException {

        TicketDto ticketDto = ticketService.getTicketById(ticketId);

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormatter.format(new Date());
        dateFormatter = new SimpleDateFormat("HH.mm");
        String currentTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String fileName = ticketDto.getName() + " " + currentDate + " at " + currentTime + ".pdf";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);

        List<ReserveDto> reserveDtos = reserveService.getReservesByTicketIdAndCanceledFalseAndStatePaid(ticketId);

        exporter.export(response, reserveDtos, ticketDto);

    }

    /**
     * Cancel reserve by reserve id reserve dto.
     *
     * @param reserveId the reserve id
     * @return the reserve dto
     */
    public ReserveDto toogleCancelByReserveId(Long reserveId) {
        ReserveDto reserveDto = reserveService.toogleCancelByReserveId(reserveId);
        reserveDto.getPurchase().setTicket(null);
        reserveDto.getPurchase().setReserves(null);
        if (reserveDto.getPurchase().getCoupon() != null) {
            reserveDto.getPurchase().getCoupon().setTickets(null);
        }
        reserveDto.getPurchase().setBuyer(null);
        return reserveDto;
    }

    /**
     * Resend email by reserve code reserve dto.
     *
     * @param reserveCode the reserve code
     * @return the reserve dto
     * @throws WannabeBackException the wannabe back exception
     * @throws MessagingException   the messaging exception
     */
    public ReserveDto resendEmailByReserveCode(String reserveCode) throws WannabeBackException, MessagingException {

        ReserveDto reserveDto = reserveService.getReserveByReserveCode(reserveCode);
        ReserveListDto reserveListDto = new ReserveListDto();
        reserveListDto.setReserveOwner(modelMapper.map(reserveDto, ReserveDtoWithQrCode.class));
        comunicacionesService.enviarCorreosReserva(reserveListDto);
        return reserveDto;

    }
}
