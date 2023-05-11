package es.timo.mc.timo.wannabeback.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Expo data response.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpoDataResponse {

    /**
     * The Id.
     */
    private String id;
    /**
     * The Status.
     */
    private String status;
}
