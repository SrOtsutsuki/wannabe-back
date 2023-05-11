package es.timo.mc.timo.wannabeback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

/**
 * The type Reserve dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveDtoWithQrCode extends ReserveDto {


    private ByteArrayResource qrCode;


}
