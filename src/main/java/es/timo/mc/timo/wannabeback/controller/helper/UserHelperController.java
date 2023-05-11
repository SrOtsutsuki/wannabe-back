package es.timo.mc.timo.wannabeback.controller.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.timo.mc.timo.wannabeback.configuration.JwtProperties;
import es.timo.mc.timo.wannabeback.model.dto.RoleDto;
import es.timo.mc.timo.wannabeback.model.dto.UserDto;
import es.timo.mc.timo.wannabeback.model.dto.request.LoginRequest;
import es.timo.mc.timo.wannabeback.model.dto.response.LoginResponse;
import es.timo.mc.timo.wannabeback.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The type User helper controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class UserHelperController {

    /**
     * The User service.
     */
    private final UserService userService;
    /**
     * The Jwt config.
     */
    private final JwtProperties jwtConfig;

    /**
     * The Authentication manager.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * The Refresh token expire time.
     */
    private Date refreshToken_expire_time;

    /**
     * Refresh token.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException the io exception
     */
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("Actualizando token");

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getPhrase());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                UserDto user = userService.getUserByUsername(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(RoleDto::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }

        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    /**
     * Login login response.
     *
     * @param request      the request
     * @param loginRequest the login request
     * @return the login response
     * @throws BadCredentialsException the bad credentials exception
     */
    public LoginResponse login(HttpServletRequest request, LoginRequest loginRequest) throws BadCredentialsException {
        log.info("El usuario es: {}", loginRequest.getUsername());
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        rememberMe(loginRequest.getRememberMe());
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getPhrase());
        String access_token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(refreshToken_expire_time)
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        return new LoginResponse(access_token, refresh_token);
    }


    /************* PRIVATE METHODS  **************/


    /**
     * The Remember Me
     *
     * @param rememberMe
     */
    private void rememberMe(Boolean rememberMe) {
        if (rememberMe) {
            //Si quiere recordar usuario el refresh token durara 30 dias
            Date date = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);
            this.refreshToken_expire_time = date;
        } else {
            //Si quiere recordar usuario el refresh token durara 1 dia
            Date date = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
            this.refreshToken_expire_time = date;
        }
    }

}
