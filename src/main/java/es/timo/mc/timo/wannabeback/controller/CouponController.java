package es.timo.mc.timo.wannabeback.controller;

import es.timo.mc.timo.wannabeback.controller.helper.CouponHelperController;
import es.timo.mc.timo.wannabeback.model.dto.CouponDto;
import es.timo.mc.timo.wannabeback.model.dto.request.CouponRequest;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Coupon controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/coupon")
@Api(tags = "Coupon")
@RequiredArgsConstructor
public class CouponController extends BaseErrorController {

    /**
     * The Coupon helper controller.
     */
    private final CouponHelperController couponHelperController;

    /**
     * Save coupon response entity.
     *
     * @param CouponRequest the coupon request
     * @return the response entity
     */
    @PostMapping("/saveCoupon")
    public ResponseEntity<CouponDto> saveCoupon(@RequestBody CouponRequest CouponRequest) {
        CouponDto Coupon = couponHelperController.saveCoupon(CouponRequest);
        return new ResponseEntity<>(Coupon, HttpStatus.CREATED);
    }

    /**
     * Delete coupons response entity.
     *
     * @param ids the ids
     * @return the response entity
     */
    @DeleteMapping("/deleteCoupons")
    public ResponseEntity<?> deleteCoupons(@RequestBody List<Long> ids) {
        couponHelperController.deleteCoupons(ids);
        return ResponseEntity.ok().build();
    }


    /**
     * Find all coupons response entity.
     *
     * @return the response entity
     */
    @GetMapping("/getAllCoupons")
    public ResponseEntity<List<CouponDto>> findAllCoupons() {
        List<CouponDto> Coupons = couponHelperController.findAllCoupons();
        return ResponseEntity.ok(Coupons);
    }

    /**
     * Edit coupon response entity.
     *
     * @param couponRequest the coupon request
     * @return the response entity
     */
    @PutMapping("/editCoupon")
    public ResponseEntity<CouponDto> editCoupon(@RequestBody CouponRequest couponRequest) {
        CouponDto couponDto = couponHelperController.editCoupon(couponRequest);
        return ResponseEntity.ok(couponDto);
    }

    /**
     * Check coupon by code and ticket id response entity.
     *
     * @param code     the code
     * @param ticketId the ticket id
     * @return the response entity
     * @throws WannabeBackException the wannabe back exception
     */
    @GetMapping("/getCouponByCodeAndTicketId")
    public ResponseEntity<CouponDto> getCouponByCodeAndTicketId(
            @RequestParam String code,
            @RequestParam Long ticketId) throws WannabeBackException {
        CouponDto couponDto = couponHelperController.checkCouponByCodeAndTicketId(code, ticketId);
        return ResponseEntity.ok(couponDto);
    }


}
