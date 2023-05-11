package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.PurchaseDto;
import es.timo.mc.timo.wannabeback.model.dto.RedsysParamsDto;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;

/**
 * The interface Redsys service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface RedsysService {


    /**
     * Generate purchase redsys params dto.
     *
     * @param purchaseDto the purchase dto
     * @return the redsys params dto
     * @throws WannabeBackException the wannabe back exception
     */
    RedsysParamsDto generatePurchase(PurchaseDto purchaseDto) throws WannabeBackException;


    String validateRedsysParams(RedsysParamsDto redsysParamsDto) throws Exception;
}
