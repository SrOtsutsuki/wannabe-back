package es.timo.mc.timo.wannabeback.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type Jwt config.
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    /**
     * The Phrase.
     */
    private String phrase;


}
