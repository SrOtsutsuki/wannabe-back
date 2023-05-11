package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.model.dto.PersonDto;
import es.timo.mc.timo.wannabeback.model.entity.Person;
import es.timo.mc.timo.wannabeback.repository.PersonRepository;
import es.timo.mc.timo.wannabeback.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 * The type Person service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    /**
     * The Person repository.
     */
    private final PersonRepository personRepository;

    /**
     * The Model mapper.
     */
    private final ModelMapper modelMapper;

    /**
     * The Entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Gets person by document.
     *
     * @param document the document
     * @return the person by document
     */
    @Override
    @Transactional(readOnly = true)
    public PersonDto getPersonByDocument(String document) {
        log.info("Buscando persona por documento: ", document);
        Person person = personRepository.findPersonByDocument(document).orElse(null);
        if (person != null) {
            return modelMapper.map(person, PersonDto.class);
        }
        return null;
    }

    /**
     * Save person person dto.
     *
     * @param personDto the person dto
     * @return the person dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public PersonDto savePerson(PersonDto personDto) {
        log.info("Guandando persona {}", personDto.getName());
        capitalizeName(personDto);
        Person person = modelMapper.map(personDto, Person.class);
        person = personRepository.save(person);
        return modelMapper.map(person, PersonDto.class);
    }

    /**
     * Edit person person dto.
     *
     * @param personDto the person dto
     * @return the person dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public PersonDto editPerson(PersonDto personDto) {
        log.info("Editando persona: {}", personDto.getName());
        Person person = null;
        if (personDto.getId() != null) {
            person = personRepository.findById(personDto.getId()).orElse(null);
        } else {
            person = personRepository.findPersonByDocument(personDto.getDocument()).orElse(null);
        }
        if (person != null) {
            person.setMail(personDto.getMail());
            person.setPhone(personDto.getPhone());
            person = personRepository.save(person);
            entityManager.flush();
            entityManager.refresh(person);
            return modelMapper.map(person, PersonDto.class);
        }
        throw new EntityNotFoundException("No se ha encontrado la persona");
    }

    /**
     * Process person person dto.
     *
     * @param personDto the person dto
     * @return the person dto
     */
    @Override
    public PersonDto processPerson(PersonDto personDto) {
        log.info("Procesando persona....");
        PersonDto personDb = getPersonByDocument(personDto.getDocument());
        //Si el servicio encuentra la persona se edita para actualizar los datos
        if (personDb != null) {
            return editPerson(personDto);
        } else {
            //Si no, se crea una nueva
            return savePerson(personDto);
        }
    }

    /**
     * Capitalize name.
     *
     * @param personDto the person dto
     */
    private void capitalizeName(PersonDto personDto) {
        String[] name = personDto.getName().split(" ");
        StringBuilder nameCapitalize = new StringBuilder();
        for (String s : name) {
            nameCapitalize.append(" ").append(StringUtils.capitalize(s.toLowerCase()));
        }
        personDto.setName(nameCapitalize.toString().trim());
    }

}
