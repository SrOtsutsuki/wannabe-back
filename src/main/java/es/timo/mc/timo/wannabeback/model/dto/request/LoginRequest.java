package es.timo.mc.timo.wannabeback.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * The type Login request.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    /**
     * The Username.
     */
    @NotNull
    private String username;
    /**
     * The Password.
     */
    @NotNull
    private String password;
    /**
     * The Remember me.
     */
    private Boolean rememberMe;

}
