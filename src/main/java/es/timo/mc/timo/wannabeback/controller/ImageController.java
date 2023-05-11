package es.timo.mc.timo.wannabeback.controller;

import es.timo.mc.timo.wannabeback.controller.helper.ImageHelperController;
import es.timo.mc.timo.wannabeback.model.dto.ImageDto;
import es.timo.mc.timo.wannabeback.model.dto.request.ImageRequest;
import es.timo.mc.timo.wannabeback.model.enums.ImageCategory;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Image controller.
 */
@RestController
@RequestMapping("/api/v1/image")
@Api(tags = "Image")
@RequiredArgsConstructor
public class ImageController extends BaseErrorController {

    /**
     * The Image helper controller.
     */
    private final ImageHelperController imageHelperController;

    /**
     * Save image response entity.
     *
     * @param imageRequest the image request
     * @return the response entity
     */
    @PostMapping("/saveImage")
    public ResponseEntity<ImageDto> saveImage(@RequestBody ImageRequest imageRequest) {
        ImageDto image = imageHelperController.saveImage(imageRequest);
        return new ResponseEntity<>(image, HttpStatus.CREATED);
    }

    /**
     * Delete images response entity.
     *
     * @param ids the ids
     * @return the response entity
     */
    @DeleteMapping("/deleteImages")
    public ResponseEntity<?> deleteImages(@RequestBody List<Long> ids) {
        imageHelperController.deleteImages(ids);
        return ResponseEntity.ok().build();
    }


    /**
     * Find all images response entity.
     *
     * @return the response entity
     */
    @GetMapping("/getAllImages")
    public ResponseEntity<List<ImageDto>> findAllImages() {
        List<ImageDto> images = imageHelperController.findAllImages();
        return ResponseEntity.ok(images);
    }

    /**
     * Find all images by category response entity.
     *
     * @param imageCategory the image category
     * @return the response entity
     */
    @GetMapping("/getAllImagesByCategory")
    public ResponseEntity<List<ImageDto>> findAllImagesByCategory(@RequestParam(required = true) ImageCategory imageCategory) {
        List<ImageDto> images = imageHelperController.findAllImagesyCategory(imageCategory);
        return ResponseEntity.ok(images);
    }
}
