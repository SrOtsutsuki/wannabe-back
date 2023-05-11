package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.model.dto.DeviceDto;
import es.timo.mc.timo.wannabeback.model.entity.Device;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import es.timo.mc.timo.wannabeback.repository.DeviceRepository;
import es.timo.mc.timo.wannabeback.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Device service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    /**
     * The Device repository.
     */
    private final DeviceRepository deviceRepository;

    /**
     * The Model mapper.
     */
    private final ModelMapper modelMapper;

    /**
     * Save device id device dto.
     *
     * @param deviceId the device id
     * @return the device dto
     * @throws WannabeBackException the wannabe back exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceDto saveDeviceId(String deviceId) throws WannabeBackException {
        log.info("Guardando deviceId: {}", deviceId);
        Device device = deviceRepository.findByDeviceId(deviceId);
        if (device == null) {
            device = new Device();
            device.setDeviceId(deviceId);
            deviceRepository.save(device);
            return modelMapper.map(device, DeviceDto.class);
        } else {
            log.error("El dispositivo ya está registrado");
            throw new WannabeBackException("El dispositivo ya está registrado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<DeviceDto> getAllDevices() {
        log.info("Obteniendo todos los devices");
        List<Device> devices = deviceRepository.findAll();
        return devices.stream().map(device -> modelMapper.map(device, DeviceDto.class)).toList();
    }
}
