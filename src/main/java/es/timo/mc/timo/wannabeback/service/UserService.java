package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.UserDto;

import java.util.List;

/**
 * The interface User service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface UserService {

    /**
     * Save user user.
     *
     * @param userDto the user dto
     * @return the user
     */
    UserDto saveUser(UserDto userDto);

    /**
     * Add role to user.
     *
     * @param username the username
     * @param roleName the role name
     */
    void addRoleToUser(String username, String roleName);

    /**
     * Gets user by username.
     *
     * @param username the username
     * @return the user by username
     */
    UserDto getUserByUsername(String username);

    /**
     * Gets all users.
     *
     * @return the all users
     */
    List<UserDto> getAllUsers();

}
