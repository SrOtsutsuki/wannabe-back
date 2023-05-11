package es.timo.mc.timo.wannabeback.repository;

import es.timo.mc.timo.wannabeback.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Coupon repository.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /**
     * Gets coupon by code.
     *
     * @param code the code
     * @return the coupon by code
     */
    Coupon getCouponByCode(String code);

    /**
     * Gets all by order by created date desc.
     *
     * @return the all by order by created date desc
     */
    List<Coupon> getAllByOrderByCreatedDateDesc();

}
