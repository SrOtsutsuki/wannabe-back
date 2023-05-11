package es.timo.mc.timo.wannabeback.service.impl;

import es.timo.mc.timo.wannabeback.model.dto.BusinessDto;
import es.timo.mc.timo.wannabeback.model.entity.Business;
import es.timo.mc.timo.wannabeback.repository.BusinessRepository;
import es.timo.mc.timo.wannabeback.service.BusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Carlos Cuesta
 * @version 1.0
 */

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {


    private final BusinessRepository businessRepository;

    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BusinessDto> getAllBusiness() {
        log.info("Consultando todos los negocios");
        List<Business> businesses = businessRepository.findAll();
        return businesses.stream().map(business -> modelMapper.map(business, BusinessDto.class)).toList();
    }

    @Override
    public BusinessDto saveBusiness(BusinessDto businessDto) {
        log.info("Guardando negocio: {}", businessDto.getName());
        Business business = modelMapper.map(businessDto, Business.class);
        business = businessRepository.save(business);
        entityManager.flush();
        entityManager.refresh(business);
        return modelMapper.map(business, BusinessDto.class);
    }
}
