package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.RoleDto;

import javax.persistence.EntityNotFoundException;

/**
 * The interface Role service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface RoleService {

    /**
     * Save role role dto.
     *
     * @param roleDto the role dto
     * @return the role dto
     */
    RoleDto saveRole(RoleDto roleDto);

    /**
     * Gets role by name.
     *
     * @param name the name
     * @return the role by name
     * @throws EntityNotFoundException the entity not found exception
     */
    RoleDto getRoleByName(String name);

}
