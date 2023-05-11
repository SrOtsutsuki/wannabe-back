package es.timo.mc.timo.wannabeback.model.dto.request;

import es.timo.mc.timo.wannabeback.model.dto.NotificationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Carlos Cuesta
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    private NotificationDto notification;
}
