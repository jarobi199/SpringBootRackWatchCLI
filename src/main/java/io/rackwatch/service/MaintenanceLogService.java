package io.rackwatch.service;

import io.rackwatch.authentication.SessionContext;
import io.rackwatch.enums.MaintenanceType;
import io.rackwatch.model.Device;
import io.rackwatch.model.MaintenanceLog;
import io.rackwatch.model.Rack;
import io.rackwatch.repository.MaintenanceLogRepository;
import io.rackwatch.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MaintenanceLogService {
    @Autowired
    private MaintenanceLogRepository maintenanceLogRepository;
    @Autowired
    private RackRepository rackRepository;

    public void addMaintenanceLog(Device device, String notes, MaintenanceType type, LocalDate performedDate, String firmwareVersionAfter, LocalDate newExpiryDate) {
        Optional<Rack> optionalRack = rackRepository.findById(device.getRackId());
        MaintenanceLog maintenanceLog = new MaintenanceLog(SessionContext.getUser().getId(), device.getId(), device.getName(), optionalRack.map(Rack::getName).orElse(null), type, performedDate, notes,
                device.getFirmwareVersion(),  firmwareVersionAfter, newExpiryDate);
        maintenanceLogRepository.save(maintenanceLog);
    }
}
