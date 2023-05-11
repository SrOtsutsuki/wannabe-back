package es.timo.mc.timo.wannabeback.controller.helper;

import es.timo.mc.timo.wannabeback.model.dto.DeviceDto;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import es.timo.mc.timo.wannabeback.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * The type Device helper controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
public class DeviceHelperController {

    /**
     * The Device service.
     */
    private final DeviceService deviceService;

    /**
     * Save device id device dto.
     *
     * @param deviceId the device id
     * @return the device dto
     * @throws WannabeBackException the wannabe back exception
     */
    public DeviceDto saveDeviceId(String deviceId) throws WannabeBackException {
        DeviceDto deviceDto = deviceService.saveDeviceId(deviceId);
        return deviceDto;
    }
}
