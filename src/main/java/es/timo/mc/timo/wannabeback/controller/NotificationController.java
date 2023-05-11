package es.timo.mc.timo.wannabeback.controller;

import es.timo.mc.timo.wannabeback.controller.helper.NotificationHelperController;
import es.timo.mc.timo.wannabeback.model.dto.NotificationDto;
import es.timo.mc.timo.wannabeback.model.dto.request.NotificationRequest;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

/**
 * The type Notification controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/notification")
@Api(tags = "Notification")
@RequiredArgsConstructor
public class NotificationController {

    /**
     * The Notification helper controller.
     */
    private final NotificationHelperController notificationHelperController;

    /**
     * Send notification response entity.
     *
     * @param notificationRequest the notification request
     * @return the response entity
     * @throws WannabeBackException the wannabe back exception
     * @throws URISyntaxException   the uri syntax exception
     */
    @PostMapping("/sendNotification")
    public ResponseEntity<NotificationDto> sendNotification(@RequestBody NotificationRequest notificationRequest) throws WannabeBackException, URISyntaxException {
        NotificationDto notificationDto = notificationHelperController.sendNotification(notificationRequest.getNotification());
        return ResponseEntity.ok(notificationDto);
    }

    /**
     * Gets notifications.
     *
     * @return the notifications
     */
    @GetMapping("/getNotifications")
    public ResponseEntity<List<NotificationDto>> getNotifications() {
        List<NotificationDto> notificationDtoList = notificationHelperController.getNotifications();
        return ResponseEntity.ok(notificationDtoList);
    }
}
