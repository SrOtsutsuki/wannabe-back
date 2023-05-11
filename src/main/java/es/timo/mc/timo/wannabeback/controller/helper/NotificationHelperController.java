package es.timo.mc.timo.wannabeback.controller.helper;

import es.timo.mc.timo.wannabeback.model.dto.NotificationDto;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import es.timo.mc.timo.wannabeback.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.List;

/**
 * The type Notification helper controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class NotificationHelperController {

    /**
     * The Notification service.
     */
    private final NotificationService notificationService;

    /**
     * Send notification notification dto.
     *
     * @param notification the notification
     * @return the notification dto
     * @throws WannabeBackException the wannabe back exception
     * @throws URISyntaxException   the uri syntax exception
     */
    public NotificationDto sendNotification(NotificationDto notification) throws WannabeBackException, URISyntaxException {
        NotificationDto notificationDto = notificationService.sendNotification(notification);
        return notificationDto;
    }

    /**
     * Gets notifications.
     *
     * @return the notifications
     */
    public List<NotificationDto> getNotifications() {

        List<NotificationDto> notificationDtoList = notificationService.getNotifications();
        return notificationDtoList;
    }
}
