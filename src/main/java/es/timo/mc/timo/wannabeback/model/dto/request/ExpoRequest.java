package es.timo.mc.timo.wannabeback.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Expo request.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpoRequest {

    /**
     * The To.
     */
    private String to;
    /**
     * The Title.
     */
    private String title;
    /**
     * The Body.
     */
    private String body;

}
