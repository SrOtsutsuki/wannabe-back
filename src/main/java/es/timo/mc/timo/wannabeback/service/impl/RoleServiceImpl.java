package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.model.dto.RoleDto;
import es.timo.mc.timo.wannabeback.model.entity.Role;
import es.timo.mc.timo.wannabeback.repository.RoleRepository;
import es.timo.mc.timo.wannabeback.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 * The type Role service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    /**
     * The Role repository.
     */
    private final RoleRepository roleRepository;

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
     * Save role role dto.
     *
     * @param roleDto the role dto
     * @return the role dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDto saveRole(RoleDto roleDto) {
        log.info("Guardando role: {}", roleDto.getName());
        Role role = modelMapper.map(roleDto, Role.class);
        role = roleRepository.save(role);
        entityManager.flush();
        entityManager.refresh(role);
        return modelMapper.map(role, RoleDto.class);

    }

    /**
     * Gets role by name.
     *
     * @param name the name
     * @return the role by name
     * @throws EntityNotFoundException the entity not found exception
     */
    @Override
    @Transactional(readOnly = true)
    public RoleDto getRoleByName(String name) {
        log.info("Buscando rol: {}", name);
        Role role = roleRepository.findByName(name);
        if (role != null) {
            return modelMapper.map(role, RoleDto.class);
        }
        log.error("No se ha encontraod el rol: {}", name);
        throw new EntityNotFoundException("Rol no encontrado");
    }
}
