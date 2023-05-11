package es.timo.mc.timo.wannabeback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * The type Person dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDto {

    /**
     * The Id.
     */
    private Long id;

    /**
     * The Name.
     */
    @NotNull
    private String name;

    /**
     * The Mail.
     */
    @NotNull
    @Email
    private String mail;

    /**
     * The Phone.
     */
    @NotNull
    @Pattern(regexp = "^[+]{0,1}[0-9]{5,20}$")
    private String phone;

    /**
     * The Document.
     */
    @NotNull
    @Pattern(regexp = "^([0-9]{8}[a-zA-Z])|[xyzXYZ][0-9]{7}[a-zA-Z]$")
    private String document;

    /**
     * The Accept lopd.
     */
    @NotNull
    private Boolean acceptLOPD;
    /**
     * The Accept comercial info.
     */
    @NotNull
    private Boolean acceptComercialInfo;

}
