package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.ReserveDto;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;

import java.util.List;

/**
 * The interface Reserve service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface ReserveService {

    /**
     * Gets reserve by reserve code.
     *
     * @param reserveCode the reserve code
     * @return the reserve by reserve code
     * @throws WannabeBackException the wannabe back exception
     */
    ReserveDto getReserveByReserveCode(String reserveCode) throws WannabeBackException;

    /**
     * Save all reserve list.
     *
     * @param reserveDto the reserve dto
     * @return the list
     */
    List<ReserveDto> saveAllReserve(List<ReserveDto> reserveDto);


    /**
     * Toogle used from reserve id reserve dto.
     *
     * @param reserveId the reserve id
     * @return the reserve dto
     */
    ReserveDto toogleUsedFromReserveId(Long reserveId);

    /**
     * Gets reserves by ticket id.
     *
     * @param ticketId  the ticket id
     * @param roleStaff the role staff
     * @return the reserves by ticket id
     */
    List<ReserveDto> getReservesByTicketId(Long ticketId, String roleStaff);

    /**
     * Gets reserves by ticket id and canceled false.
     *
     * @param ticketId the ticket id
     * @return the reserves by ticket id and canceled false
     */
    List<ReserveDto> getReservesByTicketIdAndCanceledFalseAndStatePaid(Long ticketId);

    /**
     * Toogle cancel by reserve id reserve dto.
     *
     * @param reserveId the reserve id
     * @return the reserve dto
     */
    ReserveDto toogleCancelByReserveId(Long reserveId);

    /**
     * Obtain unique reserve code string.
     *
     * @return the string
     */
    String obtainUniqueReserveCode();
}
