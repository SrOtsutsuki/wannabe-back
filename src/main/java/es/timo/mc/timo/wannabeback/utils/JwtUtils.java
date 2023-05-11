package es.timo.mc.timo.wannabeback.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import es.timo.mc.timo.wannabeback.configuration.JwtProperties;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * The type Jwtutils.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@RequiredArgsConstructor
public class JwtUtils {

    /**
     * The Jwt config.
     */
    private final JwtProperties jwtConfig;
    /**
     * The Jwt.
     */
    private String jwt;


    /**
     * Instantiates a new Jwtutils.
     *
     * @param jwt       the jwt
     * @param jwtConfig the jwt config
     */
    public JwtUtils(String jwt, JwtProperties jwtConfig) {
        this.jwt = jwt.substring("Bearer ".length());
        this.jwtConfig = jwtConfig;
    }


    /**
     * Decode jwt decoded jwt.
     *
     * @return the decoded jwt
     */
    public DecodedJWT decodeJwt() {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getPhrase());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(this.jwt);
    }

    /**
     * Obtain role staff string.
     *
     * @return the string
     */
    public String obtainRoleStaff() {

        String[] role = decodeJwt().getClaim("roles").asArray(String.class);

        return Arrays.stream(role).toList().stream().filter(s -> s.contains("ROLE_STAFF")).findFirst().orElse(null);
    }
}

