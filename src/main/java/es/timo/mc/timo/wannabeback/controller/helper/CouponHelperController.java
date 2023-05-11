package es.timo.mc.timo.wannabeback.controller.helper;

import es.timo.mc.timo.wannabeback.model.dto.CouponDto;
import es.timo.mc.timo.wannabeback.model.dto.request.CouponRequest;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import es.timo.mc.timo.wannabeback.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Coupon helper controller.
 */
@Component
@RequiredArgsConstructor
public class CouponHelperController {

    /**
     * The Coupon service.
     */
    private final CouponService couponService;

    /**
     * Save coupon coupon dto.
     *
     * @param couponRequest the coupon request
     * @return the coupon dto
     */
    public CouponDto saveCoupon(CouponRequest couponRequest) {
        return couponService.saveCoupon(couponRequest.getCoupon());
    }

    /**
     * Delete coupons.
     *
     * @param ids the ids
     */
    public void deleteCoupons(List<Long> ids) {
        couponService.deleteCoupons(ids);
    }

    /**
     * Find all coupons list.
     *
     * @return the list
     */
    public List<CouponDto> findAllCoupons() {
        return couponService.getAllCoupons();
    }


    /**
     * Edit coupon coupon dto.
     *
     * @param couponRequest the coupon request
     * @return the coupon dto
     */
    public CouponDto editCoupon(CouponRequest couponRequest) {
        return couponService.editCoupon(couponRequest.getCoupon());
    }

    /**
     * Check coupon by code and ticket id double.
     *
     * @param code     the code
     * @param ticketId the ticket id
     * @return the double
     * @throws WannabeBackException the wannabe back exception
     */
    public CouponDto checkCouponByCodeAndTicketId(String code, Long ticketId) throws WannabeBackException {
        CouponDto couponDto = couponService.checkCouponByCodeAndTicketId(code, ticketId);
        cleanData(couponDto);
        return couponDto;
    }

    /**
     * Clean data.
     *
     * @param couponDto the coupon dto
     */
    private void cleanData(CouponDto couponDto) {
        couponDto.setTickets(null);
    }
}
