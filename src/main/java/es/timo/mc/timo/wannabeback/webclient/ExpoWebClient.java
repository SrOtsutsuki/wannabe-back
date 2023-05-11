package es.timo.mc.timo.wannabeback.webclient;

import es.timo.mc.timo.wannabeback.model.dto.request.ExpoRequest;
import es.timo.mc.timo.wannabeback.model.dto.response.ExpoResponse;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

/**
 * The type Expo web client.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class ExpoWebClient {

    /**
     * The Rest template.
     */
    private final RestTemplate restTemplate;

    /**
     * The Uri.
     */
    private final String URI = "https://exp.host/--/api/v2/push/send";

    /**
     * Send notification.
     *
     * @param request the request
     * @throws URISyntaxException   the uri syntax exception
     * @throws WannabeBackException the wannabe back exception
     */
    public void sendNotification(List<ExpoRequest> request) throws URISyntaxException, WannabeBackException {
        URI uri = new URI(URI);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<List<ExpoRequest>> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ExpoResponse> result = restTemplate.postForEntity(uri, httpEntity, ExpoResponse.class);
        if (!result.getStatusCode().equals(HttpStatus.OK)) {
            log.error("Error enviando a Expo las notificaciones");
            throw new WannabeBackException("No se pudo enviar la notificaciones", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Notificacion enviada correctamente: {}", result.getBody());
    }
}
