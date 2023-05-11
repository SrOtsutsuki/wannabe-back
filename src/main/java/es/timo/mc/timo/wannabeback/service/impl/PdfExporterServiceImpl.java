package es.timo.mc.timo.wannabeback.service.impl;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import es.timo.mc.timo.wannabeback.model.dto.ReserveDto;
import es.timo.mc.timo.wannabeback.model.dto.TicketDto;
import es.timo.mc.timo.wannabeback.service.PdfExporterService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * The type Pdf exporter.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
public class PdfExporterServiceImpl implements PdfExporterService {

    /**
     * Write table header ticket.
     *
     * @param table the table
     */
    private void writeTableHeaderTicket(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLACK);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        font.setSize(9);

        cell.setPhrase(new Phrase("Nombre", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Inicio entrada", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Cierre entrada", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Lugar", font));
        table.addCell(cell);


    }

    /**
     * Write table data ticket.
     *
     * @param table     the table
     * @param ticketDto the ticket dto
     */
    private void writeTableDataTicket(PdfPTable table, TicketDto ticketDto) {

        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        table.addCell(ticketDto.getName());
        table.addCell(dateFormatter.format(ticketDto.getDate()));
        table.addCell(dateFormatter.format(ticketDto.getClosingDate()));
        table.addCell(ticketDto.getBusiness().getName());

    }


    /**
     * Write table header reserves.
     *
     * @param table the table
     */
    private void writeTableHeaderReserves(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLACK);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        font.setSize(10);

        cell.setPhrase(new Phrase("Código", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Nombre completo", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Correo", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Teléfono", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Documento", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Bebida", font));
        table.addCell(cell);

    }

    /**
     * Write table data reserves.
     *
     * @param table       the table
     * @param reserveDtos the reserve dtos
     */
    private void writeTableDataReserves(PdfPTable table, List<ReserveDto> reserveDtos) {

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.WHITE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);
        font.setSize(9);


        for (ReserveDto reserveDto : reserveDtos) {
            cell.setPhrase(new Phrase(reserveDto.getReserveCode(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(reserveDto.getPerson().getName(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(reserveDto.getPerson().getMail(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(reserveDto.getPerson().getPhone(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(reserveDto.getPerson().getDocument(), font));
            table.addCell(cell);

            cell.setPhrase(new Phrase(reserveDto.getConsummation() ? "Sí" : "No", font));
            table.addCell(cell);
        }
    }


    /**
     * Export.
     *
     * @param response    the response
     * @param reserveDtos the reserve dtos
     * @param ticketDto   the ticket dto
     * @throws DocumentException the document exception
     * @throws IOException       the io exception
     */
    public void export(HttpServletResponse response, List<ReserveDto> reserveDtos, TicketDto ticketDto) throws DocumentException, IOException {

        log.info("Generado pdf para el ticket: {}, con un total de reservas de: {}", ticketDto.getId(), reserveDtos.size());

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        Paragraph p = null;
        PdfPTable table = null;
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);

        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String currentDateTime = dateFormatter.format(ticketDto.getDate());
        String title = "List de reservas para " + ticketDto.getName() + "-" + currentDateTime;
        p = new Paragraph(title, font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        p = new Paragraph("Evento", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        p.setSpacingBefore(20);

        document.add(p);

        table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 5f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        writeTableHeaderTicket(table);
        writeTableDataTicket(table, ticketDto);

        document.add(table);

        p = new Paragraph("Reservas", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        p.setSpacingBefore(20);

        document.add(p);

        table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2.5f, 5f, 5f, 3.0f, 2.5f, 2.5f});
        table.setSpacingBefore(10);


        writeTableHeaderReserves(table);
        writeTableDataReserves(table, reserveDtos);

        document.add(table);

        document.close();

    }

}
