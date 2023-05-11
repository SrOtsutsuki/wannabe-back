package es.timo.mc.timo.wannabeback.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Image request.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequest {

    /**
     * The Base 64 image.
     */
    private String base64Image;

    /**
     * The Image category.
     */
    private String imageCategoryName;

}
