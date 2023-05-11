package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.ReserveDtoWithQrCode;
import es.timo.mc.timo.wannabeback.model.dto.ReserveListDto;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * The interface Comunicaciones service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface ComunicacionesService {

    /**
     * Send email.
     *
     * @param reserveDto the reserve dto
     * @throws MessagingException the messaging exception
     * @throws TemplateException  the template exception
     * @throws IOException        the io exception
     */
    void sendEmail(ReserveDtoWithQrCode reserveDto) throws MessagingException, TemplateException, IOException;

    /**
     * Send email.
     *
     * @param reserveDto  the reserve dto
     * @param reserveDtos the reserve dtos
     * @throws MessagingException the messaging exception
     * @throws TemplateException  the template exception
     * @throws IOException        the io exception
     */
    void sendEmail(ReserveDtoWithQrCode reserveDto, List<ReserveDtoWithQrCode> reserveDtos) throws MessagingException, TemplateException, IOException;

    /**
     * Enviar correos reserva.
     *
     * @param reserveListDto the reserve list dto
     * @throws MessagingException the messaging exception
     */
    void enviarCorreosReserva(ReserveListDto reserveListDto) throws MessagingException;
}
