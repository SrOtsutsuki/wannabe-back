package es.timo.mc.timo.wannabeback.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Login response.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    /**
     * The Access token.
     */
    private String access_token;
    /**
     * The Refresh token.
     */
    private String refresh_token;

}
