package es.timo.mc.timo.wannabeback.repository;

import es.timo.mc.timo.wannabeback.model.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Carlos Cuesta
 * @version 1.0
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Device findByDeviceId(String deviceId);
}
