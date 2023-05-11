package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.model.dto.CouponDto;
import es.timo.mc.timo.wannabeback.model.dto.TicketDto;
import es.timo.mc.timo.wannabeback.model.entity.Coupon;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import es.timo.mc.timo.wannabeback.repository.CouponRepository;
import es.timo.mc.timo.wannabeback.service.CouponService;
import es.timo.mc.timo.wannabeback.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * The type Coupon service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    /**
     * The Entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The Coupon repository.
     */
    private final CouponRepository couponRepository;

    /**
     * The Ticket service.
     */
    private final TicketService ticketService;

    /**
     * The Model mapper.
     */
    private final ModelMapper modelMapper;

    /**
     * Save coupon coupon dto.
     *
     * @param couponDto the coupon dto
     * @return the coupon dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CouponDto saveCoupon(CouponDto couponDto) {
        log.info("Guardando cupon: {}", couponDto.getCode());
        Coupon coupon = modelMapper.map(couponDto, Coupon.class);
        coupon.setTickets(ticketService.getAllById(couponDto.getTickets().stream()
                .map(TicketDto::getId).toList()));
        coupon = couponRepository.save(coupon);
        entityManager.flush();
        entityManager.refresh(coupon);
        return modelMapper.map(coupon, CouponDto.class);

    }

    /**
     * Delete coupons.
     *
     * @param ids the ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCoupons(List<Long> ids) {
        log.info("Eliminando cupones: {}", ids);
        couponRepository.deleteAllById(ids);

    }

    /**
     * Gets all coupons.
     *
     * @return the all coupons
     */
    @Override
    @Transactional(readOnly = true)
    public List<CouponDto> getAllCoupons() {
        log.info("Obteniendo todos los cupones");
        List<Coupon> coupons = couponRepository.getAllByOrderByCreatedDateDesc();
        return coupons.stream()
                .map(coupon -> modelMapper.map(coupon, CouponDto.class))
                .toList();
    }

    /**
     * Edit coupon coupon dto.
     *
     * @param couponDto the coupon dto
     * @return the coupon dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CouponDto editCoupon(CouponDto couponDto) {
        log.info("Editando cupon: {}", couponDto.getId());
        Coupon couponSaved = couponRepository.findById(couponDto.getId()).orElse(null);
        if (couponSaved != null) {
            BeanUtils.copyProperties(couponDto, couponSaved);
            couponSaved = couponRepository.save(couponSaved);
            couponSaved.setTickets(ticketService.getAllById(couponDto.getTickets().stream()
                    .map(TicketDto::getId).toList()));
            entityManager.flush();
            entityManager.refresh(couponSaved);
            return modelMapper.map(couponSaved, CouponDto.class);
        }
        throw new EntityNotFoundException("No se ha encontrado el Cupon");
    }


    /**
     * Check coupon by code and ticket id double.
     *
     * @param code     the code
     * @param ticketId the ticket id
     * @return the double
     * @throws WannabeBackException the wannabe back exception
     */
    @Override
    @Transactional(readOnly = true)
    public CouponDto checkCouponByCodeAndTicketId(String code, Long ticketId) throws WannabeBackException {
        log.info("Comprobando cupon: {} , para el ticket: {}", code, ticketId);
        Coupon coupon = couponRepository.getCouponByCode(code);
        if (coupon != null) {
            CouponDto couponDto = modelMapper.map(coupon, CouponDto.class);
            if (couponDto.isValid()) {
                if (couponDto.getTickets().stream()
                        .anyMatch(ticketDto -> ticketDto.getId() == ticketId)) {
                    return couponDto;
                } else {
                    log.error("El cupon: {} no es valido para el evento: {}", code, ticketId);
                    throw new WannabeBackException("El cupon no es valido", HttpStatus.INTERNAL_SERVER_ERROR);

                }
            } else {
                log.error("El cupon: {} ha expirado", code);
                throw new WannabeBackException("El cupon ha expirado", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            log.error("No existe el cupon: {}", code);
            throw new WannabeBackException("No existe el cupon", HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public CouponDto getCouponById(Long couponId) {
        log.info("Consultando cupon por id: {}", couponId);
        Coupon coupon = couponRepository.findById(couponId).orElse(null);
        if (coupon != null) {
            return modelMapper.map(coupon, CouponDto.class);
        }
        return null;

    }
}
