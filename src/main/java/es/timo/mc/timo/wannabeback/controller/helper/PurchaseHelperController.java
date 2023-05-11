package es.timo.mc.timo.wannabeback.controller.helper;

import es.timo.mc.timo.wannabeback.model.dto.*;
import es.timo.mc.timo.wannabeback.model.dto.request.PurchaseRequest;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import es.timo.mc.timo.wannabeback.service.CouponService;
import es.timo.mc.timo.wannabeback.service.PurchaseService;
import es.timo.mc.timo.wannabeback.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Purchase helper controller.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class PurchaseHelperController {

    /**
     * The Ticket service.
     */
    private final TicketService ticketService;

    /**
     * The Coupon service.
     */
    private final CouponService couponService;


    /**
     * The Purchase service.
     */
    private final PurchaseService purchaseService;


    /**
     * Make purchase redsys params dto.
     *
     * @param purchaseRequest the purchase request
     * @return the redsys params dto
     * @throws Exception the exception
     */
    public RedsysParamsDto makePurchase(PurchaseRequest purchaseRequest) throws Exception {

        validatePurchase(purchaseRequest, false);
        return purchaseService.savePurchase(purchaseRequest);
    }

    /**
     * Validate purchase ok purchase dto.
     *
     * @param redsysParamsDto the redsys params dto
     * @return the purchase dto
     * @throws Exception the exception
     */
    public PurchaseDto validatePurchaseOk(RedsysParamsDto redsysParamsDto) throws Exception {
        PurchaseDto purchaseDto = purchaseService.validatePurchaseOk(redsysParamsDto);
        cleanData(purchaseDto);
        return purchaseDto;
    }

    /**
     * Make free purchase purchase dto.
     *
     * @param purchaseRequest the purchase request
     * @return the purchase dto
     * @throws Exception the exception
     */
    public PurchaseDto makeFreePurchase(PurchaseRequest purchaseRequest) throws Exception {

        validatePurchase(purchaseRequest, true);
        PurchaseDto purchaseDto = purchaseService.saveFreePurchase(purchaseRequest);
        cleanData(purchaseDto);
        return purchaseDto;
    }


    /**
     * Gets all purchase.
     *
     * @return the all purchase
     */
    public List<PurchaseDto> getAllPurchase() {
        List<PurchaseDto> purchaseDtoList = purchaseService.getAllPurchase();
        purchaseDtoList.forEach(this::cleanData);
        return purchaseDtoList;
    }

    /**
     * Activate purchase purchase dto.
     *
     * @param orderId the order id
     * @return the purchase dto
     */
    public PurchaseDto activatePurchase(String orderId) throws Exception {
        PurchaseDto purchaseDto = purchaseService.activatePurchase(orderId);
        cleanData(purchaseDto);
        return purchaseDto;
    }


    /************** PRIVATE METHODS *******************************/

    /**
     * Validate purchase.
     *
     * @param purchaseRequest the purchase request
     * @param isFreePurchase  the is free purchase
     * @throws WannabeBackException the wannabe back exception
     */
    private void validatePurchase(PurchaseRequest purchaseRequest, boolean isFreePurchase) throws WannabeBackException {

        //Se cuenta los amigos y si no por defecto es una persona, el owner
        Integer totalPeople = (purchaseRequest.getFriends() != null) ? purchaseRequest.getFriends().size() + 1 : 1;
        validatePurchaseData(purchaseRequest);
        //Se valida el ticket
        validateTicket(purchaseRequest.getTicketId(), totalPeople);
        //Se valida el cupon
        if (purchaseRequest.getCouponId() != null) {
            valiateCoupon(purchaseRequest.getCouponId(), purchaseRequest.getTicketId(), totalPeople, isFreePurchase);
        }

    }

    /**
     * Validate purchase data.
     *
     * @param purchaseRequest the purchase request
     * @throws WannabeBackException the wannabe back exception
     */
    private void validatePurchaseData(PurchaseRequest purchaseRequest) throws WannabeBackException {
        List<PersonDto> personDtos = new ArrayList<>();
        personDtos.add(purchaseRequest.getOwner());
        personDtos.addAll(purchaseRequest.getFriends());
        List<String> documents = personDtos.stream().map(PersonDto::getDocument).toList();
        List<String> duplicateDocuments = documents.stream().distinct().toList();
        if (duplicateDocuments.size() != documents.size()) {
            throw new WannabeBackException("Personas duplicadas en la compra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Valiate coupon.
     *
     * @param couponId       the coupon id
     * @param ticketId       the ticket id
     * @param totalPeople    the total people
     * @param isFreePurchase the is free purchase
     * @throws WannabeBackException the wannabe back exception
     */
    private void valiateCoupon(Long couponId, Long ticketId, Integer totalPeople, boolean isFreePurchase) throws WannabeBackException {
        CouponDto couponDto = couponService.getCouponById(couponId);
        validateIfCouponIsValid(couponDto);
        validateIfCouponIsValidForTheEvent(couponDto, ticketId);
        validateIfCouponIsValidForTheTotalPeople(couponDto, totalPeople);
        if (isFreePurchase) {
            validateIfCouponIsFree(couponDto);
        }
    }

    /**
     * Validate if coupon is free.
     *
     * @param couponDto the coupon dto
     * @throws WannabeBackException the wannabe back exception
     */
    private void validateIfCouponIsFree(CouponDto couponDto) throws WannabeBackException {
        if (couponDto.getDiscount() < 100) {
            throw new WannabeBackException("A engañar a otro chaval", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Validate ticket.
     *
     * @param ticketId    the ticket id
     * @param totalPeople the total people
     * @throws WannabeBackException the wannabe back exception
     */
    private void validateTicket(Long ticketId, Integer totalPeople) throws WannabeBackException {
        TicketDto ticket = ticketService.getTicketById(ticketId);
        validateIfTicketExist(ticket);
        validateIfTicketActive(ticket);
        validateIfTicketTimeOut(ticket);
        validateIfTicketSoldOut(ticket);
        validateRemainingCapacity(ticket, totalPeople);
    }

    /**
     * Validate if ticket exist.
     *
     * @param ticketDto the ticket dto
     * @throws WannabeBackException the wannabe back exception
     */
    private void validateIfTicketExist(TicketDto ticketDto) throws WannabeBackException {
        if (ticketDto == null) {
            throw new WannabeBackException("No existe el evento", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Validate if ticket active.
     *
     * @param ticketDto the ticket dto
     * @throws WannabeBackException the wannabe back exception
     */
    private void validateIfTicketActive(TicketDto ticketDto) throws WannabeBackException {
        if (!ticketDto.getActive()) {
            throw new WannabeBackException("El evento no está activo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Validate if ticket time out.
     *
     * @param ticketDto the ticket dto
     * @throws WannabeBackException the wannabe back exception
     */
    private void validateIfTicketTimeOut(TicketDto ticketDto) throws WannabeBackException {
        if (ticketDto.getTimeOut()) {
            throw new WannabeBackException("Se ha acabado el tiempo de compra del evento", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Validate if ticket sold out.
     *
     * @param ticketDto the ticket dto
     * @throws WannabeBackException the wannabe back exception
     */
    private void validateIfTicketSoldOut(TicketDto ticketDto) throws WannabeBackException {
        if (ticketDto.getSoldOut()) {
            throw new WannabeBackException("No quedan entradas en venta para el evento", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Validate remaining capacity.
     *
     * @param ticketDto   the ticket dto
     * @param totalPeople the total people
     * @throws WannabeBackException the wannabe back exception
     */
    private void validateRemainingCapacity(TicketDto ticketDto, Integer totalPeople) throws WannabeBackException {
        if (ticketDto.getRemainingCapacity() < totalPeople) {
            throw new WannabeBackException("No quedan entradas suficientes", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Validate if coupon is valid.
     *
     * @param couponDto the coupon dto
     * @throws WannabeBackException the wannabe back exception
     */
    private void validateIfCouponIsValid(CouponDto couponDto) throws WannabeBackException {
        if (!couponDto.isValid()) {
            throw new WannabeBackException("El cupon ha expirado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Validate if coupon is valid for the event.
     *
     * @param couponDto the coupon dto
     * @param ticketId  the ticket id
     * @throws WannabeBackException the wannabe back exception
     */
    private void validateIfCouponIsValidForTheEvent(CouponDto couponDto, Long ticketId) throws WannabeBackException {
        if (couponDto.getTickets().stream().noneMatch(ticketDto -> ticketDto.getId() == ticketId)) {
            throw new WannabeBackException("El cupon no es valido para el evento", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Validate if coupon is valid for the total people.
     *
     * @param couponDto   the coupon dto
     * @param totalPeople the total people
     * @throws WannabeBackException the wannabe back exception
     */
    private void validateIfCouponIsValidForTheTotalPeople(CouponDto couponDto, Integer totalPeople) throws WannabeBackException {
        if (couponDto.getTotalPeoplePerUse() < totalPeople) {
            log.error("Cupon para un maximo numero de personas de : {} y un total de personas de: {}",
                    couponDto.getTotalPeoplePerUse(), totalPeople);
            throw new WannabeBackException("El cupon no es valido para el total de personas", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Clean data.
     *
     * @param purchaseDto the purchase dto
     */
    private void cleanData(PurchaseDto purchaseDto) {
        //Se limpian datos que no se necesitan
        purchaseDto.getReserves().forEach(reserveDto -> reserveDto.setPurchase(null));
        if (purchaseDto.getCoupon() != null) {
            purchaseDto.getCoupon().setTickets(null);
        }
    }

}
