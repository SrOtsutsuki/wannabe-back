package es.timo.mc.timo.wannabeback.repository;

import es.timo.mc.timo.wannabeback.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The interface Notification repository.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    /**
     * Find first 5 order by created date asc list.
     *
     * @return the list
     */
    @Query(value = "SELECT * FROM notification n ORDER BY n.id DESC LIMIT 5 ", nativeQuery = true)
    List<Notification> findNotificationLimit5();
}
