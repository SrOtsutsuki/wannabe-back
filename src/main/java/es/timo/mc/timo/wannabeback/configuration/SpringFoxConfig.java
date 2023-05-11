package es.timo.mc.timo.wannabeback.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * The type Spring fox config.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    /**
     * The constant AUTHORIZATION_HEADER.
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Api docket.
     *
     * @return the docket
     */
    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("es.timo.mc.timo.wannabeback.controller"))
                .build()
                .securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()))
                .apiInfo(apiInfo())
                .pathMapping("/")
                .useDefaultResponseMessages(false)
                .directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .tags(
                        new Tag("Ticket", "Endpoints for operations on Tickets"),
                        new Tag("Reserve", "Endpoints for operations on Reserves"),
                        new Tag("Image", "Endpoints for operations on Images"),
                        new Tag("Coupon", "Endpoints for operations on Coupons"),
                        new Tag("User", "Endpoints for operations on User"),
                        new Tag("Purchase", "Endpoints for operations on Purchase"),
                        new Tag("Device", "Endpoints for operations on Device"),
                        new Tag("Notification", "Endpoints for operations on Notification")
                );
    }


    /**
     * Api key api key.
     *
     * @return the api key
     */
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    /**
     * Api info api info.
     *
     * @return the api info
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Wannabe API")
                .description("api operations to work with wannabe app")
                .build();
    }

    /**
     * Security context security context.
     *
     * @return the security context
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    /**
     * Default auth list.
     *
     * @return the list
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

}
