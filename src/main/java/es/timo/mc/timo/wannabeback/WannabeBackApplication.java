package es.timo.mc.timo.wannabeback;

import es.timo.mc.timo.wannabeback.model.dto.BusinessDto;
import es.timo.mc.timo.wannabeback.model.dto.RoleDto;
import es.timo.mc.timo.wannabeback.model.dto.UserDto;
import es.timo.mc.timo.wannabeback.model.enums.Business;
import es.timo.mc.timo.wannabeback.model.enums.RoleBusiness;
import es.timo.mc.timo.wannabeback.service.BusinessService;
import es.timo.mc.timo.wannabeback.service.RoleService;
import es.timo.mc.timo.wannabeback.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Wannabe back application.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class WannabeBackApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(WannabeBackApplication.class, args);
    }

    /**
     * B crypt password encoder b crypt password encoder.
     *
     * @return the b crypt password encoder
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Gets rest template.
     *
     * @return the rest template
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * Runner command line runner.
     *
     * @param userService     the user service
     * @param roleService     the role service
     * @param businessService the business service
     * @return the command line runner
     */
    @Bean
    CommandLineRunner runner(UserService userService, RoleService roleService, BusinessService businessService) {
        return args -> {

            /*GUARDAR USUARIO APP */

            try {
                UserDto admin = userService.getUserByUsername("WannabeAdmin");
            } catch (EntityNotFoundException e) {
                roleService.saveRole(new RoleDto(null, "ROLE_ADMIN"));
                userService.saveUser(new UserDto(null, "WannabeAdmin", "folslachupa", new ArrayList<>()));
                userService.addRoleToUser("WannabeAdmin", "ROLE_ADMIN");
            }

            try {
                UserDto casinoStaff = userService.getUserByUsername("CasinoStaff");
            } catch (EntityNotFoundException e) {
                roleService.saveRole(new RoleDto(null, RoleBusiness.CASINO.getRoleName()));
                userService.saveUser(new UserDto(null, "CasinoStaff", "CasinoStaff2xxx", new ArrayList<>()));
                userService.addRoleToUser("CasinoStaff", RoleBusiness.CASINO.getRoleName());
            }

            try {
                UserDto brutalStaff = userService.getUserByUsername("BrutalStaff");
            } catch (EntityNotFoundException e) {
                roleService.saveRole(new RoleDto(null, RoleBusiness.BRUTAL.getRoleName()));
                userService.saveUser(new UserDto(null, "BrutalStaff", "BrutalStaff2xxx", new ArrayList<>()));
                userService.addRoleToUser("BrutalStaff", RoleBusiness.BRUTAL.getRoleName());
            }

            /** GUARDAR NEGOCIOS APP */

            List<BusinessDto> businessDtos = businessService.getAllBusiness();
            if (businessDtos.isEmpty()) {
                businessService.saveBusiness(new BusinessDto(Business.CASINO.getId(), Business.CASINO.getName(), "Calle falsa 123"));
                businessService.saveBusiness(new BusinessDto(Business.BRUTAL.getId(), Business.BRUTAL.getName(), "Calle falsa 123"));
            }

        };
    }


}
