package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.ReserveDto;
import es.timo.mc.timo.wannabeback.model.dto.ReserveListDto;

/**
 * The interface Qr code service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface QrCodeService {

    /**
     * Generate byte [ ].
     *
     * @param reserveDto the reserve dto
     * @return the byte [ ]
     */
    byte[] generate(ReserveDto reserveDto);

    /**
     * Generate qrs.
     *
     * @param reserveListDto the reserve list dto
     */
    void generateQrs(ReserveListDto reserveListDto);

}
