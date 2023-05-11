package es.timo.mc.timo.wannabeback.model.dto;

import es.timo.mc.timo.wannabeback.model.enums.ImageCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Image dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {

    /**
     * The Id.
     */
    private Long id;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Image category.
     */
    private ImageCategory imageCategory;
}
