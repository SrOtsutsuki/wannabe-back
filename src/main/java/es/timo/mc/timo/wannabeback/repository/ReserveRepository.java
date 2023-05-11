package es.timo.mc.timo.wannabeback.repository;

import es.timo.mc.timo.wannabeback.model.entity.Reserve;
import es.timo.mc.timo.wannabeback.model.enums.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Reserve repository.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    /**
     * Find by reserve code reserve.
     *
     * @param reserveCode the reserve code
     * @param estate      the estate
     * @return the reserve
     */
    Reserve findByReserveCodeAndCanceledFalseAndPurchaseEstate(String reserveCode, Estate estate);

    /**
     * Find by reserve code and purchase ticket business name reserve.
     *
     * @param reserveCode  the reserve code
     * @param businessName the business name
     * @return the reserve
     */
    Reserve findByReserveCodeAndPurchaseTicketBusinessName(String reserveCode, String businessName);

    /**
     * Find all by purchase ticket id list.
     *
     * @param ticketId the ticket id
     * @param estate   the estate
     * @return the list
     */
    List<Reserve> findAllByPurchaseTicketIdAndPurchaseEstateOrderByPersonNameAsc(Long ticketId, Estate estate);

    /**
     * Find all by purchase ticket id and canceled false order by person name asc list.
     *
     * @param ticketId the ticket id
     * @param estate   the estate
     * @return the list
     */
    List<Reserve> findAllByPurchaseTicketIdAndCanceledFalseAndPurchaseEstateOrderByPersonNameAsc(Long ticketId, Estate estate);

    /**
     * Find all by purchase ticket id and purchase ticket business name order by person name asc list.
     *
     * @param ticketId     the ticket id
     * @param businessName the business name
     * @param estate       the estate
     * @return the list
     */
    List<Reserve> findAllByPurchaseTicketIdAndPurchaseTicketBusinessNameAndCanceledFalseAndPurchaseEstateOrderByPersonNameAsc(Long ticketId, String businessName, Estate estate);

    /**
     * Find all by reserve code reserve.
     *
     * @param reserveCode the reserve code
     * @return the reserve
     */
    Reserve findAllByReserveCode(String reserveCode);

}
