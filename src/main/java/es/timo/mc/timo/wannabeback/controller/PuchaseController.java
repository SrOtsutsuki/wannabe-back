package es.timo.mc.timo.wannabeback.controller;

import es.timo.mc.timo.wannabeback.controller.helper.PurchaseHelperController;
import es.timo.mc.timo.wannabeback.model.dto.PurchaseDto;
import es.timo.mc.timo.wannabeback.model.dto.RedsysParamsDto;
import es.timo.mc.timo.wannabeback.model.dto.request.PurchaseRequest;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * The type Puchase controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Controller
@RequestMapping("/api/v1/purchase")
@Api(tags = "Purchase")
@Log4j2
@RequiredArgsConstructor
public class PuchaseController {

    /**
     * The Purchase helper controller.
     */
    private final PurchaseHelperController purchaseHelperController;


    /**
     * Make purchase response entity.
     *
     * @param purchaseRequest the purchase request
     * @return the response entity
     * @throws Exception the exception
     */
    @PostMapping("/makePurchase")
    public ResponseEntity<RedsysParamsDto> makePurchase(@Valid @RequestBody PurchaseRequest purchaseRequest) throws Exception {
        RedsysParamsDto redsysParamsDto = purchaseHelperController.makePurchase(purchaseRequest);
        return new ResponseEntity<>(redsysParamsDto, HttpStatus.CREATED);

    }

    /**
     * Make free purchase response entity.
     *
     * @param purchaseRequest the purchase request
     * @return the response entity
     * @throws Exception the exception
     */
    @PostMapping("/makeFreePurchase")
    public ResponseEntity<PurchaseDto> makeFreePurchase(@Valid @RequestBody PurchaseRequest purchaseRequest) throws Exception {
        if (purchaseRequest.getCouponId() == null) {
            throw new WannabeBackException("Es obligatorio disponer de un cupon", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PurchaseDto purchaseDto = purchaseHelperController.makeFreePurchase(purchaseRequest);
        return new ResponseEntity<>(purchaseDto, HttpStatus.CREATED);

    }

    /**
     * Gets all purchase.
     *
     * @return the all purchase
     */
    @GetMapping("/getAllPurchase")
    public ResponseEntity<List<PurchaseDto>> getAllPurchase() {
        List<PurchaseDto> purchaseDtoList = purchaseHelperController.getAllPurchase();
        return ResponseEntity.ok(purchaseDtoList);
    }

    /**
     * Activate purchase response entity.
     *
     * @param orderId the order id
     * @return the response entity
     * @throws Exception the exception
     */
    @PostMapping("/activatePurchase")
    public ResponseEntity<PurchaseDto> activatePurchase(@RequestParam String orderId) throws Exception {
        PurchaseDto purchaseDto = purchaseHelperController.activatePurchase(orderId);
        return new ResponseEntity<>(purchaseDto, HttpStatus.CREATED);

    }


    /**
     * Validate purchase ok response entity.
     *
     * @param redsysParamsDto the redsys params dto
     * @return the response entity
     * @throws Exception the exception
     */
    @PostMapping("/validatePurchaseOk")
    public ResponseEntity<PurchaseDto> validatePurchaseOk(@RequestBody RedsysParamsDto redsysParamsDto) throws Exception {
        PurchaseDto purchaseDto = purchaseHelperController.validatePurchaseOk(redsysParamsDto);
        return new ResponseEntity<>(purchaseDto, HttpStatus.CREATED);
    }

//    @PostMapping("/validatePurchaseKo")

}
