package es.timo.mc.timo.wannabeback.model.enums;

import lombok.Getter;

/**
 * @author Carlos Cuesta
 * @version 1.0
 */
@Getter
public enum RoleBusiness {

    CASINO("ROLE_STAFF_CASINO", Business.CASINO),
    BRUTAL("ROLE_STAFF_BRUTAL", Business.BRUTAL);

    private final String roleName;

    private final Business business;


    RoleBusiness(String roleName, Business business) {
        this.roleName = roleName;
        this.business = business;
    }


}
