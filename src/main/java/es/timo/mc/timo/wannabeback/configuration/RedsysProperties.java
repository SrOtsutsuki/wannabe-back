package es.timo.mc.timo.wannabeback.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type Redsys properties.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "redsys.params")
@Data
public class RedsysProperties {

    /**
     * The Merchan code.
     */
    private String merchan_code;
    /**
     * The Terminal.
     */
    private String terminal;
    /**
     * The Signature.
     */
    private String signature;

}
