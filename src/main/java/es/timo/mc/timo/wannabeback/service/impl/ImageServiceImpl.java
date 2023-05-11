package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.model.dto.ImageDto;
import es.timo.mc.timo.wannabeback.model.entity.Image;
import es.timo.mc.timo.wannabeback.model.enums.ImageCategory;
import es.timo.mc.timo.wannabeback.repository.ImageRepository;
import es.timo.mc.timo.wannabeback.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * The type Image service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    /**
     * The Image repository.
     */
    private final ImageRepository imageRepository;

    /**
     * The Model mapper.
     */
    private final ModelMapper modelMapper;

    /**
     * The constant CARPET.
     */
    private static final String CARPET = "images";

    /**
     * The constant PATH_SEPARATOR.
     */
    private static final String PATH_SEPARATOR = "/";

    /**
     * The Entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save image image dto.
     *
     * @param imageBase64 the image base 64
     * @return the image dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImageDto saveImage(String imageBase64, String imageCategory) {
        String imageName = saveImageInDisk(imageBase64);
        log.info("Guardando imagen: {}", imageName);
        Image imageEntity = modelMapper.map(ImageDto.builder().name(imageName).imageCategory(ImageCategory.fromString(imageCategory)).build(), Image.class);
        return modelMapper.map(imageRepository.save(imageEntity), ImageDto.class);
    }

    /**
     * Delete images list.
     *
     * @param ids the ids
     * @return the list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteImages(List<Long> ids) {

        log.info("Borrando imagenes: {}", ids.toString());
        List<Image> imagesEntity = imageRepository.findAllById(ids);
        List<ImageDto> imagesDto = imagesEntity.stream().map(image -> modelMapper.map(image, ImageDto.class)).toList();
        imagesDto.forEach(image -> {
            imageRepository.delete(modelMapper.map(image, Image.class));
            entityManager.flush();
            if (deleteImageFromDisk(image)) {
                log.info("Imagen eliminada: {}", image.getName());
            }
        });
    }

    /**
     * Gets all images.
     *
     * @return the all images
     */
    @Override
    @Transactional(readOnly = true)
    public List<ImageDto> getAllImages() {
        log.info("Obteniendo todas las imagenes");
        List<Image> images = imageRepository.findAllByOrderByCreatedDateDesc();
        log.info("Imagenes obtenidas: {}", images.size());
        return images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageDto> getAllImagesByCategory(ImageCategory imageCategory) {
        log.info("Obteniendo todas las imagenes por categoria: {}", imageCategory);
        List<Image> images = imageRepository.findAllByImageCategory(imageCategory);
        log.info("Imagenes obtenidas: {}", images.size());
        return images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
    }

    /************** PRIVATE METHODS *************************/

    /**
     * Get base 64 extension string.
     *
     * @param base64File the base 64 file
     * @return the string
     */
    private String getBase64Extension(String base64File) {
        return switch (base64File) {
            //check image's extension
            case "data:image/jpeg;base64" -> ".jpeg";
            case "data:image/png;base64" -> ".png";
            case "data:image/jpg;base64" -> ".jpg";
            case "data:image/gif;base64" -> ".gif";
            default -> null;
        };
    }

    /**
     * Save image.
     *
     * @param base64Image the base 64 image
     * @return the string
     */
    private String saveImageInDisk(String base64Image) {
        String imgName = null;
        try {
            String[] base64 = base64Image.split(",");
            imgName = UUID.randomUUID() + getBase64Extension(base64[0]);
            byte[] imageByte = Base64.decodeBase64(base64[1]);
            Path directory = Paths.get(
                    System.getProperty("user.home") + File.separator + CARPET + File.separator + imgName);
            Files.createDirectories(Paths.get(System.getProperty("user.home") + File.separator + CARPET));
            Files.write(directory, imageByte);
            log.info("Imagen creada en: {}", directory.toString());
            return imgName;
        } catch (IOException e) {
            log.error("Error al guardar la images : {}", e.getMessage());
            return imgName;
        }
    }

    /**
     * Delete image from disk boolean.
     *
     * @param imageDto the image dto
     * @return the boolean
     */
    private Boolean deleteImageFromDisk(ImageDto imageDto) {
        File file = new File(System.getProperty("user.home") + File.separator + CARPET + File.separator + imageDto.getName());
        try {
            return Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            log.error("Error al borrar la imagen : {} , causa: {}", imageDto.getName(), e.getMessage());
            return Boolean.FALSE;
        }

    }
}
