package es.timo.mc.timo.wannabeback.controller;

import es.timo.mc.timo.wannabeback.controller.helper.DeviceHelperController;
import es.timo.mc.timo.wannabeback.model.dto.DeviceDto;
import es.timo.mc.timo.wannabeback.model.dto.request.DeviceRequest;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Device controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/device")
@Api(tags = "Device")
@RequiredArgsConstructor
public class DeviceController extends BaseErrorController {

    /**
     * The Device helper controller.
     */
    private final DeviceHelperController deviceHelperController;

    /**
     * Save device id response entity.
     *
     * @param deviceRequest the device request
     * @return the response entity
     * @throws WannabeBackException the wannabe back exception
     */
    @PostMapping("/saveDeviceId")
    public ResponseEntity<DeviceDto> saveDeviceId(@RequestBody DeviceRequest deviceRequest) throws WannabeBackException {
        DeviceDto deviceDto = deviceHelperController.saveDeviceId(deviceRequest.getDevice().getDeviceId());
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceDto);
    }

}
