package es.timo.mc.timo.wannabeback.repository;

import es.timo.mc.timo.wannabeback.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * The interface Entrada repository.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {


    /**
     * Find active tickets and deleted false list.
     *
     * @param date the date
     * @return the list
     */
    @Query("Select t from Ticket t where :date >= t.startSaleDate and :date < t.endSaleDate ORDER BY t.date ASC")
    List<Ticket> findActiveTickets(@Param("date") Date date);


    /**
     * Find active tickets by business id and deleted false list.
     *
     * @param id   the id
     * @param date the date
     * @return the list
     */
    @Query("Select t  from Ticket t where :date >= t.startSaleDate and :date< t.closingDate and t.business.id = :id ORDER BY t.date ASC")
    List<Ticket> findActiveTicketsByBusinessId(@Param("id") Long id, @Param("date") Date date);

    /**
     * Find all by deleted false list.
     *
     * @return the list
     */
    List<Ticket> findAllByOrderByDateDesc();


    /**
     * Find all by date after order by date asc list.
     *
     * @param date the date
     * @return the list
     */
    List<Ticket> findAllByClosingDateAfterOrderByDateAsc(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date);

    /**
     * Find all by closing date after and business name order by date asc list.
     *
     * @param date         the date
     * @param businessName the business name
     * @return the list
     */
    List<Ticket> findAllByClosingDateAfterAndBusinessNameOrderByDateAsc(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, String businessName);


}
