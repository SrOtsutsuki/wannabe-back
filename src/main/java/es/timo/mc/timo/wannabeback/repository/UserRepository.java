package es.timo.mc.timo.wannabeback.repository;

import es.timo.mc.timo.wannabeback.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Carlos Cuesta
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
