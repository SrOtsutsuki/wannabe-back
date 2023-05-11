package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.model.dto.RoleDto;
import es.timo.mc.timo.wannabeback.model.dto.UserDto;
import es.timo.mc.timo.wannabeback.model.entity.User;
import es.timo.mc.timo.wannabeback.repository.UserRepository;
import es.timo.mc.timo.wannabeback.service.RoleService;
import es.timo.mc.timo.wannabeback.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type User service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    /**
     * The User repository.
     */
    private final UserRepository userRepository;

    /**
     * The Role service.
     */
    private final RoleService roleService;

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
     * The B crypt password encoder.
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Save user user dto.
     *
     * @param userDto the user dto
     * @return the user dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto saveUser(UserDto userDto) {
        log.info("Guardando usuario: {}", userDto.getUsername());
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        User user = modelMapper.map(userDto, User.class);
        user = userRepository.save(user);
        entityManager.flush();
        entityManager.refresh(user);
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Add role to user.
     *
     * @param username the username
     * @param roleName the role name
     * @throws EntityNotFoundException the entity not found exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRoleToUser(String username, String roleName) {
        log.info("Asignado al usuario: {} , el rol: {}", username, roleName);
        UserDto user = getUserByUsername(username);
        RoleDto role = roleService.getRoleByName(roleName);
        user.getRoles().add(role);
        userRepository.save(modelMapper.map(user, User.class));
    }

    /**
     * Gets user by username.
     *
     * @param username the username
     * @return the user by username
     */
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        log.info("Buscando usuario: {}", username);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return modelMapper.map(user, UserDto.class);
        }
        log.error("Usuario: {} , no encontrado", username);
        throw new EntityNotFoundException("Usuario no encontrado");
    }

    /**
     * Gets all users.
     *
     * @return the all users
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        log.info("Buscando todos los usuarios");
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("Usuario no encontrado en la base de datos: {}", username);
            throw new UsernameNotFoundException("El usuario " + username + " no existe");
        } else {
            log.info("Usuario encontrado en la base de datos: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
