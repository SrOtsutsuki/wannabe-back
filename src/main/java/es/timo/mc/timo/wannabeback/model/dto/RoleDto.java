package es.timo.mc.timo.wannabeback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Role dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {

    /**
     * The Id.
     */
    private String id;
    /**
     * The Name.
     */
    private String name;
}
