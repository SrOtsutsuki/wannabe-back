package es.timo.mc.timo.wannabeback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Bussines dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessDto {


    /**
     * The Id.
     */
    private Long id;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Address.
     */
    private String address;

}
