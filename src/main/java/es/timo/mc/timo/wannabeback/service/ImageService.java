package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.ImageDto;
import es.timo.mc.timo.wannabeback.model.enums.ImageCategory;

import java.util.List;

/**
 * The interface Image service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface ImageService {

    /**
     * Save image image dto.
     *
     * @param imageBase64   the image base 64
     * @param imageCategory the image category
     * @return the image dto
     */
    ImageDto saveImage(String imageBase64, String imageCategory);

    /**
     * Delete images list.
     *
     * @param ids the ids
     */
    void deleteImages(List<Long> ids);

    /**
     * Gets all images.
     *
     * @return the all images
     */
    List<ImageDto> getAllImages();

    /**
     * Gets all images by category.
     *
     * @param imageCategory the image category
     * @return the all images by category
     */
    List<ImageDto> getAllImagesByCategory(ImageCategory imageCategory);
}
