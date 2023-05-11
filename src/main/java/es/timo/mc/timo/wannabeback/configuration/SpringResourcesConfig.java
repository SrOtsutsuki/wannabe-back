package es.timo.mc.timo.wannabeback.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * The type Spring security.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Configuration
@EnableWebMvc
public class SpringResourcesConfig implements WebMvcConfigurer {

    /**
     * The constant CARPET.
     */
    private static final String CARPET = "images";

    /**
     * The constant PATH_IMAGES.
     */
    private static final String PATH_IMAGES = System.getProperty("user.home") + File.separator + CARPET + File.separator;

    /**
     * Add resource handlers.
     *
     * @param registry the registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + PATH_IMAGES);
    }
}