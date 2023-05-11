package es.timo.mc.timo.wannabeback.model.dto.request;

import es.timo.mc.timo.wannabeback.model.dto.DeviceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Device request.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequest {

    /**
     * The Device id.
     */
    private DeviceDto device;
}
