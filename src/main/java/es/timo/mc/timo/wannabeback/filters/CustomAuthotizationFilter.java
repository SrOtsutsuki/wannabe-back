package es.timo.mc.timo.wannabeback.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.timo.mc.timo.wannabeback.configuration.JwtProperties;
import es.timo.mc.timo.wannabeback.model.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The type Custom authotization filter.
 */
@Slf4j
public class CustomAuthotizationFilter extends OncePerRequestFilter {

    /**
     * The Jwt config.
     */
    private final JwtProperties jwtConfig;

    /**
     * Instantiates a new Custom authotization filter.
     *
     * @param jwtConfig the jwt config
     */
    public CustomAuthotizationFilter(JwtProperties jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * Do filter internal.
     *
     * @param request     the request
     * @param response    the response
     * @param filterChain the filter chain
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/user/login") || request.getServletPath().equals("/api/v1/user/refresh-token")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getPhrase());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {

                    log.error("Error login in: {}", exception.getMessage());
                    ErrorResponse errorResponse = new ErrorResponse(UNAUTHORIZED.value(), exception.getMessage());
                    errorResponse.setError(exception.getClass().getSimpleName());
                    errorResponse.setTimestamp(new Date());
                    errorResponse.setPath(request.getServletPath());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setStatus(UNAUTHORIZED.value());
                    new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
                }

            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
