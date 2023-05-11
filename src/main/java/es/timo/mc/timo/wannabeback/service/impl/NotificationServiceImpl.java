package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.model.dto.DeviceDto;
import es.timo.mc.timo.wannabeback.model.dto.NotificationDto;
import es.timo.mc.timo.wannabeback.model.dto.request.ExpoRequest;
import es.timo.mc.timo.wannabeback.model.entity.Notification;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import es.timo.mc.timo.wannabeback.repository.NotificationRepository;
import es.timo.mc.timo.wannabeback.service.DeviceService;
import es.timo.mc.timo.wannabeback.service.NotificationService;
import es.timo.mc.timo.wannabeback.webclient.ExpoWebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Notification service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationServiceImpl implements NotificationService {

    /**
     * The Notification repository.
     */
    private final NotificationRepository notificationRepository;

    /**
     * The Entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The Model mapper.
     */
    private final ModelMapper modelMapper;

    /**
     * The Device service.
     */
    private final DeviceService deviceService;

    /**
     * The Expo web client.
     */
    private final ExpoWebClient expoWebClient;

    /**
     * Send notification notification dto.
     *
     * @param notificationDto the notification dto
     * @return the notification dto
     * @throws WannabeBackException the wannabe back exception
     * @throws URISyntaxException   the uri syntax exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public NotificationDto sendNotification(NotificationDto notificationDto) throws WannabeBackException, URISyntaxException {
        log.info("Guardando la notificacion: {}", notificationDto.getTitle());
        Notification notification = modelMapper.map(notificationDto, Notification.class);
        notification = notificationRepository.save(notification);
        entityManager.flush();
        entityManager.refresh(notification);
        NotificationDto notificationDtoSaved = modelMapper.map(notification, NotificationDto.class);
        List<DeviceDto> devices = deviceService.getAllDevices();
        List<ExpoRequest> request = transformExpoRequest(devices, notificationDtoSaved);
        expoWebClient.sendNotification(request);
        return notificationDtoSaved;

    }

    /**
     * Gets notifications.
     *
     * @return the notifications
     */
    @Override
    public List<NotificationDto> getNotifications() {
        log.info("Obteniento las ultimas 5 notificaciones");
        List<Notification> notifications = notificationRepository.findNotificationLimit5();
        return notifications.stream().map(notification -> modelMapper.map(notification, NotificationDto.class)).toList();
    }

    /**
     * Transform expo request list.
     *
     * @param devices              the devices
     * @param notificationDtoSaved the notification dto saved
     * @return the list
     */
    private List<ExpoRequest> transformExpoRequest(List<DeviceDto> devices, NotificationDto notificationDtoSaved) {
        List<ExpoRequest> request = new ArrayList<>();
        ExpoRequest expoRequest;
        for (DeviceDto device : devices) {
            expoRequest = new ExpoRequest();
            expoRequest.setBody(notificationDtoSaved.getBody());
            expoRequest.setTitle(notificationDtoSaved.getTitle());
            expoRequest.setTo(device.getDeviceId());
            request.add(expoRequest);
        }
        return request;
    }
}
