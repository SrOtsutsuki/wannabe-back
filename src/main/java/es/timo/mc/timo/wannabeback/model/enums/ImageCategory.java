package es.timo.mc.timo.wannabeback.model.enums;

import lombok.Getter;

/**
 * The enum Category.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Getter
public enum ImageCategory {

    /**
     * The Main pic.
     */
    MAIN_PIC("MAIN_PIC", "MAIN"),

    /**
     * The Resverve pic.
     */
    RESERVE_PIC("RESERVE_PIC", "RESERVA"),

    /**
     * The Ticket pic.
     */
    TICKET_PIC("TICKET_PIC", "EVENTO");


    /**
     * The Name.
     */
    private final String name;

    /**
     * The Description.
     */
    private final String description;

    /**
     * Instantiates a new Category.
     *
     * @param name        the name
     * @param description the description
     */
    ImageCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static ImageCategory fromString(String name) {
        for (ImageCategory i : ImageCategory.values()) {
            if (i.name.equalsIgnoreCase(name)) {
                return i;
            }
        }
        return null;
    }
}
