package es.timo.mc.timo.wannabeback.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author Carlos Cuesta
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RedsysParamsDto {


    @JsonProperty("Ds_SignatureVersion")
    private String dsSignatureVersion = "HMAC_SHA256_V1";

    @JsonProperty("Ds_MerchantParameters")
    private String dsMerchantParameters;

    @JsonProperty("Ds_Signature")
    private String dsSignature;


}
