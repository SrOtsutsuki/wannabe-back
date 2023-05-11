package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.PersonDto;

/**
 * The interface Person service.
 */
public interface PersonService {

    /**
     * Gets person by document.
     *
     * @param document the document
     * @return the person by document
     */
    PersonDto getPersonByDocument(String document);

    /**
     * Save person person dto.
     *
     * @param personDto the person dto
     * @return the person dto
     */
    PersonDto savePerson(PersonDto personDto);

    /**
     * Edit person person dto.
     *
     * @param personDto the person dto
     * @return the person dto
     */
    PersonDto editPerson(PersonDto personDto);

    /**
     * Process person person dto.
     *
     * @param personDto the person dto
     * @return the person dto
     */
    PersonDto processPerson(PersonDto personDto);

}
