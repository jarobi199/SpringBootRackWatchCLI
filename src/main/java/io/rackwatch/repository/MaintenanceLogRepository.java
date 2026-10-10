package io.rackwatch.repository;

import io.rackwatch.model.MaintenanceLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceLogRepository extends MongoRepository<MaintenanceLog, String> {
    List<MaintenanceLog> findByDeviceId(String deviceId);
    List<MaintenanceLog> findByUserIdOrderByPerformedDateDesc(String userId);
}
