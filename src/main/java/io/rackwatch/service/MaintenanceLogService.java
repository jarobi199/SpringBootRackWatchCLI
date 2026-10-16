package io.rackwatch.service;

import io.rackwatch.enums.MaintenanceType;
import io.rackwatch.model.Device;
import io.rackwatch.repository.MaintenanceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MaintenanceLogService {
    @Autowired
    private MaintenanceLogRepository maintenanceLogRepository;

    public void addMaintenanceLog(Device device, String notes, MaintenanceType type, LocalDate performedDate, String firmwareVersionAfter, LocalDate newExpiryDate) {
        //TODO: Add code here
    }
}
