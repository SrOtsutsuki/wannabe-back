package es.timo.mc.timo.wannabeback.repository;

import es.timo.mc.timo.wannabeback.model.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Purchase repository.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    /**
     * Find all by ticket id list.
     *
     * @param ticketId the ticket id
     * @return the list
     */
    List<Purchase> findAllByTicketId(Long ticketId);

    /**
     * Find by order id purchase.
     *
     * @param orderId the order id
     * @return the purchase
     */
    Purchase findByOrderId(String orderId);

    /**
     * Find all order by date desc list.
     *
     * @return the list
     */
    List<Purchase> findAllByOrderByDateDesc();
}
