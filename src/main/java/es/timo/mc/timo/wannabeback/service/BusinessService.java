package es.timo.mc.timo.wannabeback.service;

import es.timo.mc.timo.wannabeback.model.dto.BusinessDto;

import java.util.List;

/**
 * @author Carlos Cuesta
 * @version 1.0
 */
public interface BusinessService {

    List<BusinessDto> getAllBusiness();

    BusinessDto saveBusiness(BusinessDto businessDto);

}
