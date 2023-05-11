package es.timo.mc.timo.wannabeback.repository;

import es.timo.mc.timo.wannabeback.model.entity.Image;
import es.timo.mc.timo.wannabeback.model.enums.ImageCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Image repository.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    /**
     * Find all by image category list.
     *
     * @param imageCategory the image category
     * @return the list
     */
    List<Image> findAllByImageCategory(ImageCategory imageCategory);

    /**
     * Find all by order by created date desc list.
     *
     * @return the list
     */
    List<Image> findAllByOrderByCreatedDateDesc();
}
