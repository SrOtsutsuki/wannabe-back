package es.timo.mc.timo.wannabeback.controller.helper;

import es.timo.mc.timo.wannabeback.model.dto.ImageDto;
import es.timo.mc.timo.wannabeback.model.dto.request.ImageRequest;
import es.timo.mc.timo.wannabeback.model.enums.ImageCategory;
import es.timo.mc.timo.wannabeback.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Image helper controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ImageHelperController {

    /**
     * The Image service.
     */
    private final ImageService imageService;

    /**
     * Save image image dto.
     *
     * @param imageRequest the image request
     * @return the image dto
     */
    public ImageDto saveImage(ImageRequest imageRequest) {
        return imageService.saveImage(imageRequest.getBase64Image(), imageRequest.getImageCategoryName());
    }

    /**
     * Delete images.
     *
     * @param ids the ids
     */
    public void deleteImages(List<Long> ids) {
        imageService.deleteImages(ids);
    }

    /**
     * Find all images list.
     *
     * @return the list
     */
    public List<ImageDto> findAllImages() {
        return imageService.getAllImages();
    }

    /**
     * Find all imagesy category list.
     *
     * @param imageCategory the image category
     * @return the list
     */
    public List<ImageDto> findAllImagesyCategory(ImageCategory imageCategory) {
        return imageService.getAllImagesByCategory(imageCategory);
    }
}
