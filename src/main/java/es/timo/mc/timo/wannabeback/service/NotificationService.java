package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.NotificationDto;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;

import java.net.URISyntaxException;
import java.util.List;

/**
 * The interface Notification service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface NotificationService {

    /**
     * Send notification notification dto.
     *
     * @param notificationDto the notification dto
     * @return the notification dto
     */
    NotificationDto sendNotification(NotificationDto notificationDto) throws WannabeBackException, URISyntaxException;

    /**
     * Gets notifications.
     *
     * @return the notifications
     */
    List<NotificationDto> getNotifications();
}
