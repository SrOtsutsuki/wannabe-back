package es.timo.mc.timo.wannabeback.model.enums;

import lombok.Getter;

/**
 * The enum Business.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Getter
public enum Business {

    /**
     * Casino business.
     */
    CASINO(1L, "CASINO"),
    /**
     * Brutal business.
     */
    BRUTAL(2L, "BRUTAL");

    /**
     * The Id.
     */
    private final Long id;
    /**
     * The Name.
     */
    private final String name;

    /**
     * Instantiates a new Business.
     *
     * @param id   the id
     * @param name the name
     */
    Business(Long id , String name){
        this.id = id;
        this.name = name;
    }
}


