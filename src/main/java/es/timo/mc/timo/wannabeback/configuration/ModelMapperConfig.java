package es.timo.mc.timo.wannabeback.configuration;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Model mapper configuration.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Model mapper model mapper.
     *
     * @return the model mapper
     */
    @Bean
    ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setImplicitMappingEnabled(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }


}
