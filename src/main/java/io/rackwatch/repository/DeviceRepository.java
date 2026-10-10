package io.rackwatch.repository;

import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {
    List<Device> findByUserId(String userId);
    List<Device> findByRackId(String rackId);
    List<Device> findByUserIdAndStatus(String userId, DeviceStatus status);
}
