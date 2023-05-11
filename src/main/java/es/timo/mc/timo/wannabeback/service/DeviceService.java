package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.DeviceDto;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;

import java.util.List;

/**
 * The interface Device service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface DeviceService {

    /**
     * Save device id device dto.
     *
     * @param deviceId the device id
     * @return the device dto
     * @throws WannabeBackException the wannabe back exception
     */
    DeviceDto saveDeviceId(String deviceId) throws WannabeBackException;

    /**
     * Gets all devices.
     *
     * @return the all devices
     */
    List<DeviceDto> getAllDevices();

}
