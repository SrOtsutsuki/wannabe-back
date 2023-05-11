package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.model.dto.ReserveDto;
import es.timo.mc.timo.wannabeback.model.entity.Reserve;
import es.timo.mc.timo.wannabeback.model.enums.Estate;
import es.timo.mc.timo.wannabeback.model.enums.RoleBusiness;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import es.timo.mc.timo.wannabeback.repository.ReserveRepository;
import es.timo.mc.timo.wannabeback.service.ReserveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Reserve service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService {

    /**
     * The Reserve repository.
     */
    private final ReserveRepository reserveRepository;

    /**
     * The Model mapper.
     */
    private final ModelMapper modelMapper;


    /**
     * Gets reserve by reserve code.
     *
     * @param reserveCode the reserve code
     * @return the reserve by reserve code
     * @throws WannabeBackException the wannabe back exception
     */
    @Override
    @Transactional(readOnly = true)
    public ReserveDto getReserveByReserveCode(String reserveCode) throws WannabeBackException {
        log.info("Consultado reserva por codigo: {}", reserveCode);

        Reserve reserve = reserveRepository.findByReserveCodeAndCanceledFalseAndPurchaseEstate(reserveCode, Estate.PAID);

        if (reserve != null) {
            ReserveDto reserveDto = modelMapper.map(reserve, ReserveDto.class);
            reserveDto.getPurchase().setReserves(null);
            return reserveDto;
        }
        throw new WannabeBackException("No se ha encontrado la reserva", HttpStatus.NOT_FOUND);
    }

    /**
     * Save all reserve list.
     *
     * @param reserveDto the reserve dto
     * @return the list
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<ReserveDto> saveAllReserve(List<ReserveDto> reserveDto) {
        log.info("Guardando todas las reservas: {}", reserveDto.size());
        List<Reserve> reserves = reserveDto.stream().map(reserve -> modelMapper.map(reserve, Reserve.class)).toList();
        reserves = reserveRepository.saveAll(reserves);
        return reserves.stream().map(reserve -> modelMapper.map(reserve, ReserveDto.class)).toList();
    }

    /**
     * Toogle used from reserve id reserve dto.
     *
     * @param reserveId the reserve id
     * @return the reserve dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReserveDto toogleUsedFromReserveId(Long reserveId) {
        log.info("Cambiando estado del uso de la reserva: {}", reserveId);
        Reserve reserve = reserveRepository.findById(reserveId).orElse(null);
        if (reserve != null) {
            reserve.setUsed(!reserve.getUsed());
            reserve = reserveRepository.save(reserve);
            return modelMapper.map(reserve, ReserveDto.class);
        }
        throw new EntityNotFoundException("No existe la reserva");
    }

    /**
     * Gets reserves by ticket id.
     *
     * @param ticketId  the ticket id
     * @param roleStaff the role staff
     * @return the reserves by ticket id
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReserveDto> getReservesByTicketId(Long ticketId, String roleStaff) {
        log.info("Consultando reservas del ticket: {} y por rol: {}", ticketId, roleStaff);
        List<Reserve> reserves = new ArrayList<>();
        if (roleStaff == null) {
            reserves = reserveRepository.findAllByPurchaseTicketIdAndPurchaseEstateOrderByPersonNameAsc(ticketId, Estate.PAID);
        } else if (roleStaff.equals(RoleBusiness.CASINO.getRoleName())) {
            reserves = reserveRepository.findAllByPurchaseTicketIdAndPurchaseTicketBusinessNameAndCanceledFalseAndPurchaseEstateOrderByPersonNameAsc(ticketId,
                    RoleBusiness.CASINO.getBusiness().getName(), Estate.PAID);
        } else if (roleStaff.equals(RoleBusiness.BRUTAL.getRoleName())) {
            reserves = reserveRepository.findAllByPurchaseTicketIdAndPurchaseTicketBusinessNameAndCanceledFalseAndPurchaseEstateOrderByPersonNameAsc(ticketId,
                    RoleBusiness.BRUTAL.getBusiness().getName(), Estate.PAID);
        }

        return reserves.stream()
                .map(reserve -> modelMapper.map(reserve, ReserveDto.class))
                .toList();
    }

    /**
     * Gets reserves by ticket id and canceled false.
     *
     * @param ticketId the ticket id
     * @return the reserves by ticket id and canceled false
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReserveDto> getReservesByTicketIdAndCanceledFalseAndStatePaid(Long ticketId) {
        log.info("Consultando reservas del ticket: {}", ticketId);
        List<Reserve> reserves = reserveRepository.findAllByPurchaseTicketIdAndCanceledFalseAndPurchaseEstateOrderByPersonNameAsc(ticketId, Estate.PAID);
        return reserves.stream()
                .map(reserve -> modelMapper.map(reserve, ReserveDto.class))
                .toList();
    }

    /**
     * Toogle cancel by reserve id reserve dto.
     *
     * @param reserveId the reserve id
     * @return the reserve dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReserveDto toogleCancelByReserveId(Long reserveId) {
        log.info("Cambiando estado del cancelado de la reserva: {}", reserveId);
        Reserve reserve = reserveRepository.findById(reserveId).orElse(null);
        if (reserve != null) {
            reserve.setCanceled(!reserve.getCanceled());
            reserve = reserveRepository.save(reserve);
            return modelMapper.map(reserve, ReserveDto.class);
        }
        throw new EntityNotFoundException("No existe la reserva");
    }

    /**
     * Obtain unique reserve code string.
     *
     * @return the string
     */
    @Override
    @Transactional(readOnly = true)
    public String obtainUniqueReserveCode() {
        int trys = 0;
        String uniqueCode = null;
        Reserve reserve = null;
        log.info("Obteniendo código de reserva único");
        do {
            trys++;
            log.info("Intento número: {}", trys);
            uniqueCode = generateUniqueCode();
            reserve = reserveRepository.findAllByReserveCode(uniqueCode);
        } while (reserve != null);
        log.info("Código único encontrado: {}", uniqueCode);
        return uniqueCode;
    }


    /**
     * Generate unique code string.
     *
     * @return the string
     */
    private String generateUniqueCode() {
        UUID idOne = UUID.randomUUID();
        UUID idTwo = UUID.randomUUID();
        UUID idThree = UUID.randomUUID();
        UUID idFour = UUID.randomUUID();

        String time = idOne.toString().replace("-", "");
        String time2 = idTwo.toString().replace("-", "");
        String time3 = idThree.toString().replace("-", "");
        String time4 = idFour.toString().replace("-", "");

        StringBuffer data = new StringBuffer();
        data.append(time);
        data.append(time2);
        data.append(time3);
        data.append(time4);

        SecureRandom random = new SecureRandom();
        int beginIndex = random.nextInt(100);       //Begin index + length of your string < data length
        int endIndex = beginIndex + 8;            //Length of string which you want

        String code = data.substring(beginIndex, endIndex).toUpperCase();
        return code;
    }
}
