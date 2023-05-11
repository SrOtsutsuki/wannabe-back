package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.model.dto.*;
import es.timo.mc.timo.wannabeback.model.dto.request.PurchaseRequest;
import es.timo.mc.timo.wannabeback.model.entity.Purchase;
import es.timo.mc.timo.wannabeback.model.entity.Reserve;
import es.timo.mc.timo.wannabeback.model.enums.Estate;
import es.timo.mc.timo.wannabeback.model.exception.WannabeBackException;
import es.timo.mc.timo.wannabeback.repository.PurchaseRepository;
import es.timo.mc.timo.wannabeback.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Purchase service.
 *
 * @author Carlos Cuesta
 * @version 1.0
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    /**
     * The Purchase repository.
     */
    private final PurchaseRepository purchaseRepository;

    /**
     * The Model mapper.
     */
    private final ModelMapper modelMapper;


    /**
     * The Person service.
     */
    private final PersonService personService;

    /**
     * The Ticket service.
     */
    private final TicketService ticketService;

    /**
     * The Coupon service.
     */
    private final CouponService couponService;

    /**
     * The Comunicaciones service.
     */
    private final ComunicacionesService comunicacionesService;

    /**
     * The Redsys service.
     */
    private final RedsysService redsysService;

    /**
     * The Reserve service.
     */
    private final ReserveService reserveService;

    /**
     * The Entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save purchase redsys params dto.
     *
     * @param purchaseRequest the purchase request
     * @return the redsys params dto
     * @throws Exception the exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = MessagingException.class, propagation = Propagation.REQUIRED)
    public RedsysParamsDto savePurchase(PurchaseRequest purchaseRequest) throws Exception {

        PurchaseDto purchaseDto = createPurchaseWithReserve(purchaseRequest);

        //COMPRA
        RedsysParamsDto redsysParamsDto = redsysService.generatePurchase(purchaseDto);

        return redsysParamsDto;

    }

    /**
     * Save free purchase purchase dto.
     *
     * @param purchaseRequest the purchase request
     * @return the purchase dto
     * @throws Exception the exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = MessagingException.class, propagation = Propagation.REQUIRED)
    public PurchaseDto saveFreePurchase(PurchaseRequest purchaseRequest) throws Exception {
        log.info("Guardando reserva gratis con el cupon: {}, para el comprador: {}", purchaseRequest.getCouponId(), purchaseRequest.getOwner().getName());
        PurchaseDto purchaseDto = createPurchaseWithReserve(purchaseRequest);
        Purchase purchase = purchaseRepository.findById(purchaseDto.getId()).orElse(null);
        if (purchase != null) {
            purchase.setEstate(Estate.PAID);
            purchaseRepository.save(purchase);
            purchaseDto.setEstate(Estate.PAID);
            sendReserves(purchaseDto);
            return purchaseDto;
        }
        throw new WannabeBackException("Error al comprar entrada", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Validate purchase ok purchase dto.
     *
     * @param redsysParamsDto the redsys params dto
     * @return the purchase dto
     * @throws Exception the exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseDto validatePurchaseOk(RedsysParamsDto redsysParamsDto) throws Exception {
        log.info("Validando compra despues de redsys, parametros: {}", redsysParamsDto);
        String orderId = redsysService.validateRedsysParams(redsysParamsDto);
        Purchase purchase = purchaseRepository.findByOrderId(orderId);
        if (purchase != null) {
            purchase.setEstate(Estate.PAID);
            purchaseRepository.save(purchase);
            purchase = purchaseRepository.findByOrderId(orderId);
            PurchaseDto purchaseDto = modelMapper.map(purchase, PurchaseDto.class);
            sendReserves(purchaseDto);
            return purchaseDto;
        }
        throw new WannabeBackException("OrderId no encontrado", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gets all purchase.
     *
     * @return the all purchase
     */
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseDto> getAllPurchase() {
        log.info("Obteniendo todas las compras");
        List<Purchase> purchases = purchaseRepository.findAllByOrderByDateDesc();
        log.info("Compras obtenidas : {}", purchases.size());
        return purchases.stream()
                .map(purchase -> modelMapper.map(purchase, PurchaseDto.class))
                .toList();

    }

    /**
     * Activate purchase purchase dto.
     *
     * @param orderId the order id
     * @return the purchase dto
     * @throws WannabeBackException the wannabe back exception
     * @throws MessagingException   the messaging exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = MessagingException.class, propagation = Propagation.REQUIRED)
    public PurchaseDto activatePurchase(String orderId) throws WannabeBackException, MessagingException {
        log.info("Activando compra: {}", orderId);
        Purchase purchase = purchaseRepository.findByOrderId(orderId);
        if (purchase != null) {
            purchase.setEstate(Estate.PAID);
            purchaseRepository.save(purchase);
            PurchaseDto purchaseDto = modelMapper.map(purchase, PurchaseDto.class);
            sendReserves(purchaseDto);
            return purchaseDto;
        }
        log.error("Compra con orderId: {}, no encontrada", orderId);
        throw new WannabeBackException("No se ha encontrado esa compra", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Create reserves list.
     *
     * @param owner   the owner
     * @param friends the friends
     * @return the list
     */
    private List<ReserveDto> createReserves(PersonDto owner, List<PersonDto> friends) {
        List<ReserveDto> reserves = new ArrayList<>();
        reserves.add(new ReserveDto(owner));
        if (friends != null && !friends.isEmpty()) {
            friends.forEach(personDto -> reserves.add(new ReserveDto(personDto)));
        }
        reserves.forEach(reserveDto -> reserveDto.setReserveCode(reserveService.obtainUniqueReserveCode()));
        return reserves;
    }

    /**
     * Send reserves.
     *
     * @param purchaseDto the purchase dto
     * @throws MessagingException the messaging exception
     */
    private void sendReserves(PurchaseDto purchaseDto) throws MessagingException {
        ReserveListDto reserveListDto = new ReserveListDto();
        ReserveDtoWithQrCode reserveOwner = purchaseDto.getReserves().stream()
                .filter(reserveDto -> reserveDto.getPerson().getId() == purchaseDto.getBuyer().getId())
                .findFirst()
                .map(reserveDto -> modelMapper.map(reserveDto, ReserveDtoWithQrCode.class))
                .orElse(null);
        List<ReserveDtoWithQrCode> reserveFriends = purchaseDto.getReserves().stream()
                .filter(reserveDto -> reserveDto.getPerson().getId() != purchaseDto.getBuyer().getId())
                .map(reserveDto -> modelMapper.map(reserveDto, ReserveDtoWithQrCode.class))
                .toList();

        reserveListDto.setReserveFriends(reserveFriends);
        reserveListDto.setReserveOwner(reserveOwner);
        comunicacionesService.enviarCorreosReserva(reserveListDto);
    }


    /**
     * Create purchase with reserve purchase dto.
     *
     * @param purchaseRequest the purchase request
     * @return the purchase dto
     */
    private PurchaseDto createPurchaseWithReserve(PurchaseRequest purchaseRequest) {
        log.info("Creando compra para el ticket: {}, y la persona: {}"
                , purchaseRequest.getTicketId()
                , purchaseRequest.getOwner().getName());

        PurchaseDto purchaseDto = new PurchaseDto();
        List<PersonDto> friends = null;
        //Se guarda el owner
        PersonDto owner = personService.processPerson(purchaseRequest.getOwner());
        //Si tiene amigos se guardan
        if (purchaseRequest.getFriends() != null && !purchaseRequest.getFriends().isEmpty()) {
            friends = purchaseRequest.getFriends().stream().map(personService::processPerson).toList();
        }

        //Se crean las reservas
        List<ReserveDto> reserveDtos = createReserves(owner, friends);

        //Se añade el ticket
        purchaseDto.setTicket(ticketService.getTicketById(purchaseRequest.getTicketId()));

        //Se añade el cupon si tiene
        if (purchaseRequest.getCouponId() != null) {
            purchaseDto.setCoupon(couponService.getCouponById(purchaseRequest.getCouponId()));
        }
        //Se añade el owner a la compra
        purchaseDto.setBuyer(owner);

        //Se crea la compra
        Purchase purchase = modelMapper.map(purchaseDto, Purchase.class);

        //Se añaden las reservas
        for (ReserveDto reserveDto : reserveDtos) {
            Reserve reserve = modelMapper.map(reserveDto, Reserve.class);
            purchase.addReserve(reserve);
        }

        //Se guarda la compra
        purchase = purchaseRepository.save(purchase);

        //se genera el orderId
        LocalDate fecha = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String fechaFormateada = fecha.format(formatter);
        String orderId = fechaFormateada + "OR" + purchase.getId();
        if (orderId.length() > 12) {
            orderId = orderId.substring(0, 12);
        }
        purchase.setOrderId(orderId);
        purchaseRepository.save(purchase);

        purchaseDto = modelMapper.map(purchase, PurchaseDto.class);

        log.info("Compra generada: {}", purchaseDto);

        return purchaseDto;

    }
}
