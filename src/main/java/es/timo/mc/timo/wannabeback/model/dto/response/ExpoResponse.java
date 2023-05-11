package es.timo.mc.timo.wannabeback.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Expo response.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpoResponse {

    /**
     * The Data.
     */
    private List<ExpoDataResponse> data;

}
