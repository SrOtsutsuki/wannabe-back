package es.timo.mc.timo.wannabeback.controller;

import com.lowagie.text.DocumentException;
import es.timo.mc.timo.wannabeback.controller.helper.ReserveHelperController;
import es.timo.mc.timo.wannabeback.model.dto.ReserveDto;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * The type Reserve controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Controller
@RequestMapping("/api/v1/reserve")
@Api(tags = "Reserve")
@Log4j2
@RequiredArgsConstructor
public class ReserveController {

    /**
     * The Reserve helper controller.
     */
    private final ReserveHelperController reserveHelperController;

    /**
     * Gets reserve by reserve code.
     *
     * @param reserveCode the reserve code
     * @return the reserve by reserve code
     * @throws WannabeBackException the wannabe back exception
     */
    @GetMapping("/getReserveByReserveCode")
    public ResponseEntity<ReserveDto> getReserveByReserveCode(@RequestParam String reserveCode) throws WannabeBackException {
        ReserveDto reserveDto = reserveHelperController.getReserveByReserveCode(reserveCode);
        return ResponseEntity.ok(reserveDto);
    }

    /**
     * Toogle used from reserve id response entity.
     *
     * @param reserveId the reserve id
     * @return the response entity
     */
    @GetMapping("/toogleUsedFromReserveId")
    public ResponseEntity<ReserveDto> toogleUsedFromReserveId(@RequestParam Long reserveId) {
        ReserveDto reserveDto = reserveHelperController.toogleUsedFromReserveId(reserveId);
        return ResponseEntity.ok(reserveDto);
    }

    /**
     * Gets reserves by ticket id.
     *
     * @param ticketId the ticket id
     * @param request  the request
     * @return the reserves by ticket id
     */
    @GetMapping("/getReservesByTicketId")
    public ResponseEntity<List<ReserveDto>> getReservesByTicketId(@RequestParam Long ticketId, HttpServletRequest request) {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        List<ReserveDto> reserveDtoList = reserveHelperController.getReservesByTicketId(ticketId, jwt);
        return ResponseEntity.ok(reserveDtoList);
    }

    /**
     * Generate pdf with reseres by ticket id.
     *
     * @param response the response
     * @param ticketId the ticket id
     * @throws DocumentException the document exception
     * @throws IOException       the io exception
     */
    @GetMapping("/generatePdfWithReseresByTicketId")
    public void generatePdfWithReseresByTicketId(HttpServletResponse response, @RequestParam Long ticketId) throws DocumentException, IOException {
        reserveHelperController.generatePdfWithReseresByTicketId(response, ticketId);

    }

    /**
     * Cancel reserve by reserve id response entity.
     *
     * @param reserveId the reserve id
     * @return the response entity
     */
    @GetMapping("/toogleCancelByReserveId")
    public ResponseEntity<ReserveDto> toogleCancelByReserveId(@RequestParam Long reserveId) {
        ReserveDto reserveDto = reserveHelperController.toogleCancelByReserveId(reserveId);
        return ResponseEntity.ok(reserveDto);
    }

    /**
     * Resend email by reserve code response entity.
     *
     * @param reserveCode the reserve code
     * @return the response entity
     */
    @GetMapping("/resendEmailByReserveCode")
    public ResponseEntity<ReserveDto> resendEmailByReserveCode(@RequestParam String reserveCode) throws WannabeBackException, MessagingException {
        ReserveDto reserveDto = reserveHelperController.resendEmailByReserveCode(reserveCode);
        return ResponseEntity.ok(reserveDto);
    }
}
