package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.PurchaseDto;
import es.timo.mc.timo.wannabeback.model.dto.RedsysParamsDto;
import es.timo.mc.timo.wannabeback.model.dto.request.PurchaseRequest;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;

import javax.mail.MessagingException;
import java.util.List;

/**
 * The interface Purchase service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface PurchaseService {


    /**
     * Save purchase redsys params dto.
     *
     * @param purchaseDto the purchase dto
     * @return the redsys params dto
     * @throws Exception the exception
     */
    RedsysParamsDto savePurchase(PurchaseRequest purchaseDto) throws Exception;


    /**
     * Save purchase purchase dto.
     *
     * @param purchaseRequests the purchase requests
     * @return the purchase dto
     * @throws Exception the exception
     */
    PurchaseDto saveFreePurchase(PurchaseRequest purchaseRequests) throws Exception;

    /**
     * Validate redsys params purchase dto.
     *
     * @param redsysParamsDto the redsys params dto
     * @return the purchase dto
     * @throws Exception the exception
     */
    PurchaseDto validatePurchaseOk(RedsysParamsDto redsysParamsDto) throws Exception;

    /**
     * Gets all purchase.
     *
     * @return the all purchase
     */
    List<PurchaseDto> getAllPurchase();

    /**
     * Activate purchase purchase dto.
     *
     * @param orderId the order id
     * @return the purchase dto
     */
    PurchaseDto activatePurchase(String orderId) throws WannabeBackException, MessagingException;
}
