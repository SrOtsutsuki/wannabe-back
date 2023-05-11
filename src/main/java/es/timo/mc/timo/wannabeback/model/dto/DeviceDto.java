package es.timo.mc.timo.wannabeback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Device dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceDto {


    /**
     * The Id.
     */
    private Long id;

    /**
     * The Device id.
     */
    private String deviceId;

}
