package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.WannabeBackApplication;
import es.timo.mc.timo.wannabeback.model.dto.ReserveDtoWithQrCode;
import es.timo.mc.timo.wannabeback.model.dto.ReserveListDto;
import es.timo.mc.timo.wannabeback.service.ComunicacionesService;
import es.timo.mc.timo.wannabeback.service.QrCodeService;
import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The type Comunicaciones service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class ComunicacionesServiceImpl implements ComunicacionesService {

    /**
     * The Java mail sender.
     */
    private final JavaMailSender javaMailSender;

    /**
     * The Qr code service.
     */
    private final QrCodeService qrCodeService;

    /**
     * The constant CARPET.
     */
    private static final String CARPET = "images";

    /**
     * The constant IMG_NAME.
     */
    private static final String IMG_NAME = "qr_code";

    @Value("${dominio}")
    private String dominio;


    /**
     * Send email.
     *
     * @param reserveDto the reserve dto
     * @throws MessagingException the messaging exception
     */
    @Override
    public void sendEmail(ReserveDtoWithQrCode reserveDto) {
        MimeMessage mimeMessage = null;
        try {
            mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setSubject("¡Ya estan listas tus entradas " + new StringBuilder().appendCodePoint(0x1F942) + "!", "UTF-8");
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy // HH:mm", new Locale("es", "ES"));
            sdf.setTimeZone(TimeZone.getTimeZone("CET"));

            Map<String, String> map = new HashMap<String, String>();

            //FIXME:: CAMBIAR ESTO CUANDO SE TENGA HTTPS
            map.put("BANNER_IMG", "TicketImg");
            map.put("TICKET_NAME", reserveDto.getPurchase().getTicket().getName().toUpperCase());
            map.put("FORMAT_DATE", sdf.format(reserveDto.getPurchase().getTicket().getDate()).toUpperCase());
            map.put("BUSINESS_NAME", reserveDto.getPurchase().getTicket().getBusiness().getName());
            map.put("QR", "QrCode");
            map.put("RESERVE_CODE", reserveDto.getReserveCode());
            map.put("PERSON_NAME", reserveDto.getPerson().getName().toUpperCase());
            map.put("PERSONA_MAIL", reserveDto.getPerson().getMail());
            map.put("PERSON_PHONE", reserveDto.getPerson().getPhone());
            map.put("PERSON_DOCUMENT", reserveDto.getPerson().getDocument().toUpperCase());
            map.put("TICKET_PRICE", reserveDto.getPurchase().getTicket().getPrice().toString());
            map.put("TOTAL_PURCHASE", reserveDto.getPurchase().getTotal().toString());

            String mailBody = transforHtml(map, "casinoReserva.ftl");
            helper.setText(mailBody, true); // Use this or above line.
            helper.setTo(reserveDto.getPerson().getMail());
            helper.addAttachment(reserveDto.getPerson().getName().replace(" ", "_")
                    + "_" + reserveDto.getId()
                    + ".png", reserveDto.getQrCode());
            insertarQrCode(reserveDto, helper);
            insertarTicketImg(reserveDto.getPurchase().getTicket().getTicketPic().getName(), helper);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);

    }

    /**
     * Send email.
     *
     * @param reserveOwner   the reserve owner
     * @param reserveFrineds the reserve frineds
     */
    @Override
    public void sendEmail(ReserveDtoWithQrCode reserveOwner, List<ReserveDtoWithQrCode> reserveFrineds) {
        MimeMessage mimeMessage = null;
        try {
            mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setSubject("¡Ya estan listas tus entradas " + new StringBuilder().appendCodePoint(0x1F942) + "!", "UTF-8");
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy // HH:mm", new Locale("es", "ES"));
            sdf.setTimeZone(TimeZone.getTimeZone("CET"));

            Map<String, String> map = new HashMap<String, String>();

            //FIXME:: CAMBIAR ESTO CUANDO SE TENGA HTTPS
            map.put("BANNER_IMG", "TicketImg");
            map.put("TICKET_NAME", reserveOwner.getPurchase().getTicket().getName().toUpperCase());
            map.put("FORMAT_DATE", sdf.format(reserveOwner.getPurchase().getTicket().getDate()).toUpperCase());
            map.put("BUSINESS_NAME", reserveOwner.getPurchase().getTicket().getBusiness().getName());
            map.put("QR", "QrCode");
            map.put("RESERVE_CODE", reserveOwner.getReserveCode());
            map.put("PERSON_NAME", reserveOwner.getPerson().getName().toUpperCase());
            map.put("PERSONA_MAIL", reserveOwner.getPerson().getMail());
            map.put("PERSON_PHONE", reserveOwner.getPerson().getPhone());
            map.put("PERSON_DOCUMENT", reserveOwner.getPerson().getDocument().toUpperCase());
            map.put("TICKET_PRICE", reserveOwner.getPurchase().getTicket().getPrice().toString());
            map.put("TOTAL_PURCHASE", reserveOwner.getPurchase().getTotal().toString());

            String mailBody = transforHtml(map, "casinoReserva.ftl");

            helper.setText(mailBody, true); // Use this or above line.
            helper.setTo(reserveOwner.getPerson().getMail());
            helper.addAttachment(reserveOwner.getPerson().getName().replace(" ", "_")
                    + "_" + reserveOwner.getId()
                    + ".png", reserveOwner.getQrCode());
            reserveFrineds.forEach(reserveDtoWithQrCode -> {
                try {
                    helper.addAttachment(reserveDtoWithQrCode.getPerson().getName().replace(" ", "_")
                            + "_" + reserveDtoWithQrCode.getId()
                            + ".png", reserveDtoWithQrCode.getQrCode());
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
            insertarQrCode(reserveOwner, helper);
            insertarTicketImg(reserveOwner.getPurchase().getTicket().getTicketPic().getName(), helper);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
        borrarImagenTemporal();
    }

    /**
     * Insertar qr code.
     *
     * @param reserveOwner the reserve owner
     * @param helper       the helper
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    private void insertarQrCode(ReserveDtoWithQrCode reserveOwner, MimeMessageHelper helper) throws IOException, MessagingException {
        MimeBodyPart imgPart = new MimeBodyPart();
        crearImagenTemporal(reserveOwner.getQrCode());
        // imageFile is the file containing the image
        imgPart.attachFile(new File(System.getProperty("user.home") + File.separator + CARPET + File.separator + IMG_NAME));
        // "XXX" below matches "XXX" above in html code
        imgPart.setHeader("Content-ID", "<QrCode>");
        imgPart.setHeader("Content-Type", "image/png");
        helper.getMimeMultipart().addBodyPart(imgPart);
    }

    private void insertarTicketImg(String imgName, MimeMessageHelper helper) throws IOException, MessagingException {
        MimeBodyPart imgPart = new MimeBodyPart();
        // imageFile is the file containing the image
        imgPart.attachFile(new File(System.getProperty("user.home") + File.separator + CARPET + File.separator + imgName));
        // "XXX" below matches "XXX" above in html code
        String[] imagParts = imgName.split("\\.");
        imgPart.setHeader("Content-ID", "<TicketImg>");
        imgPart.setHeader("Content-Type", "image/" + imagParts[1]);
        helper.getMimeMultipart().addBodyPart(imgPart);
    }

    /**
     * Enviar correos reserva.
     *
     * @param reserveListDto the reserve list dto
     * @throws MessagingException the messaging exception
     */
    @Override
    @Async
    public void enviarCorreosReserva(ReserveListDto reserveListDto) {

        qrCodeService.generateQrs(reserveListDto);
        sendEmail(reserveListDto.getReserveOwner(), reserveListDto.getReserveFriends());
        if (reserveListDto.getReserveFriends() != null && !reserveListDto.getReserveFriends().isEmpty()) {
            reserveListDto.getReserveFriends().forEach(this::sendEmail);
        }
    }

    /**
     * Transfor html string.
     *
     * @param dataMap      the data map
     * @param templateName the template name
     * @return the string
     * @throws TemplateException the template exception
     * @throws IOException       the io exception
     */
    public String transforHtml(Map<String, String> dataMap, String templateName) throws TemplateException, IOException {
        // Crear instancia de configuración
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        // Establecer codificación
        configuration.setDefaultEncoding("UTF-8");
        Template template = null;
        try {
            // Los archivos de plantilla FTL están unificados bajo el paquete
            InputStream is = WannabeBackApplication.class.getClassLoader().getResourceAsStream("plantillas/" + templateName);
            // Obtener plantilla
            String platilla = IOUtils.toString(is, StandardCharsets.UTF_8);
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", platilla);
            configuration.setTemplateLoader(stringTemplateLoader);
            template = configuration.getTemplate("template", "utf-8");

        } catch (TemplateNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (MalformedTemplateNameException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringWriter stringWriter = new StringWriter();
        template.process(dataMap, stringWriter);
        return stringWriter.toString();
    }


    /**
     * Crear imagen temporal.
     *
     * @param qrCode the qr code
     * @throws IOException the io exception
     */
    private void crearImagenTemporal(ByteArrayResource qrCode) throws IOException {
        Path directory = Paths.get(
                System.getProperty("user.home") + File.separator + CARPET + File.separator + IMG_NAME);
        Files.createDirectories(Paths.get(System.getProperty("user.home") + File.separator + CARPET));
        Files.write(directory, qrCode.getByteArray());
    }

    /**
     * Borrar imagen temporal.
     */
    private void borrarImagenTemporal() {
        File file = new File(System.getProperty("user.home") + File.separator + CARPET + File.separator + IMG_NAME);
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            log.error("Error al borrar la imagen : {} , causa: {}", IMG_NAME, e.getMessage());
        }
    }

}
