package es.timo.mc.timo.wannabeback.repository;

import es.timo.mc.timo.wannabeback.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Person repository.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * Find person by document optional.
     *
     * @param document the document
     * @return the optional
     */
    Optional<Person> findPersonByDocument(String document);

}
