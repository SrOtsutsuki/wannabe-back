package es.timo.mc.timo.wannabeback.configuration;

import es.timo.mc.timo.wannabeback.filters.CustomAuthotizationFilter;
import es.timo.mc.timo.wannabeback.model.enums.RoleBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Spring security config.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {


    /**
     * The constant AUTH_WHITE_LIST.
     */
    private static final String[] AUTH_WHITE_LIST = {
            "/api/v1/user/login/**",
            "/api/v1/user/refresh-token/**",
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/images/**"
    };

    /**
     * The User details service.
     */
    private final UserDetailsService userDetailsService;
    /**
     * The B crypt password encoder.
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * The Jwt config.
     */
    private final JwtProperties jwtConfig;

    /**
     * Authentication manager authentication manager.
     *
     * @return the authentication manager
     * @throws Exception the exception
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(daoAuthenticationProvider());
    }

    /**
     * Filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /** USUARIOS Y SWAGGER */
        http.authorizeRequests().antMatchers(AUTH_WHITE_LIST).permitAll();
        /**CUPONES*/
        http.authorizeRequests().antMatchers("/api/v1/coupon/getCouponByCodeAndTicketId").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/coupon/**").hasAnyAuthority("ROLE_ADMIN");
        /** IMAGENES*/
        http.authorizeRequests().antMatchers("/api/v1/image/**").hasAnyAuthority("ROLE_ADMIN");
        /** RESERVAS */
        http.authorizeRequests().antMatchers("/api/v1/reserve/generatePdfWithReseresByTicketId").hasAnyAuthority("ROLE_ADMIN", RoleBusiness.CASINO.getRoleName(), RoleBusiness.BRUTAL.getRoleName());
        http.authorizeRequests().antMatchers("/api/v1/reserve/toogleUsedFromReserveId").hasAnyAuthority("ROLE_ADMIN", RoleBusiness.CASINO.getRoleName(), RoleBusiness.BRUTAL.getRoleName());
        http.authorizeRequests().antMatchers("/api/v1/reserve/getReserveByReserveCode").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/reserve/resendEmailByReserveCode").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/reserve/getReservesByTicketId").hasAnyAuthority("ROLE_ADMIN", RoleBusiness.CASINO.getRoleName(), RoleBusiness.BRUTAL.getRoleName());
        http.authorizeRequests().antMatchers("/api/v1/reserve/toogleCancelByReserveId").hasAnyAuthority("ROLE_ADMIN");
        /**PURCHASE */
        http.authorizeRequests().antMatchers("/api/v1/purchase/getAllPurchase").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/api/v1/purchase/activatePurchase").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/api/v1/purchase/makeFreePurchase").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/purchase/makePurchase").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/purchase/validatePurchaseOk").permitAll();
        /** DEVICE */
        http.authorizeRequests().antMatchers("/api/v1/device/**").permitAll();
        /** NOTIFICATION */
        http.authorizeRequests().antMatchers("/api/v1/notification/sendNotification").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/api/v1/notification/getNotifications").permitAll();
        /** EVENTOS */
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/ticket/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/ticket/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(new CustomAuthotizationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Dao authentication provider dao authentication provider.
     *
     * @return the dao authentication provider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }


    /**
     * Cors configurer web mvc configurer.
     *
     * @return the web mvc configurer
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .exposedHeaders("content-disposition");
            }
        };
    }
}

