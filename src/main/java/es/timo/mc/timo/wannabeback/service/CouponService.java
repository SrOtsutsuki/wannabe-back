package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.CouponDto;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;

import java.util.List;

/**
 * The type Coupon service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface CouponService {

    /**
     * Save coupon coupon dto.
     *
     * @param couponDto the coupon dto
     * @return the coupon dto
     */
    CouponDto saveCoupon(CouponDto couponDto);

    /**
     * Delete coupons.
     *
     * @param ids the ids
     */
    void deleteCoupons(List<Long> ids);

    /**
     * Gets all coupons.
     *
     * @return the all coupons
     */
    List<CouponDto> getAllCoupons();


    /**
     * Edit coupon coupon dto.
     *
     * @param couponDto the coupon dto
     * @return the coupon dto
     */
    CouponDto editCoupon(CouponDto couponDto);


    /**
     * Check coupon by code and ticket id double.
     *
     * @param code     the code
     * @param ticketId the ticket id
     * @return the double
     * @throws WannabeBackException the wannabe back exception
     */
    CouponDto checkCouponByCodeAndTicketId(String code, Long ticketId) throws WannabeBackException;

    CouponDto getCouponById(Long couponId);
}
