package es.timo.mc.timo.wannabeback.service;

import com.lowagie.text.DocumentException;
import es.timo.mc.timo.wannabeback.model.dto.ReserveDto;
import es.timo.mc.timo.wannabeback.model.dto.TicketDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * The interface Pdf exporter service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface PdfExporterService {

    /**
     * Export.
     *
     * @param response    the response
     * @param reserveDtos the reserve dtos
     * @param ticketDto   the ticket dto
     * @throws DocumentException the document exception
     * @throws IOException       the io exception
     */
    void export(HttpServletResponse response, List<ReserveDto> reserveDtos, TicketDto ticketDto) throws DocumentException, IOException;

}
