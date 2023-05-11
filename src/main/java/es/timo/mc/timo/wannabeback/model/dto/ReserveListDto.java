package es.timo.mc.timo.wannabeback.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Reserve list dto.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReserveListDto {

    /**
     * The Reserve owner.
     */
    private ReserveDtoWithQrCode reserveOwner;
    /**
     * The Reserve friends.
     */
    private List<ReserveDtoWithQrCode> reserveFriends = new ArrayList<>();
}
