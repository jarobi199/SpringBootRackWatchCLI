package io.rackwatch.service;

import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableConfiguration;
import io.github.kusoroadeolu.clique.configuration.TableType;
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
import java.util.List;
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

    public List<MaintenanceLog> getAllMaintenanceLogsByDevice(Device device) {
        return maintenanceLogRepository.findByDeviceId(device.getId());
    }

    public void viewAllMaintenanceLogs() {
        List<MaintenanceLog> maintenanceLogs =  maintenanceLogRepository.findByUserIdOrderByPerformedDateDesc(SessionContext.getUser().getId());
        if(!maintenanceLogs.isEmpty()) {
            System.out.println("| LIST OF MAINTENANCE LOGS |");
            displayMaintenanceLogs(maintenanceLogs);
        }
        else
        {
            System.out.println("There are no maintenance logs.");
        }
    }

    private void displayMaintenanceLogs(List<MaintenanceLog> maintenanceLogs) {
        TableConfiguration config = TableConfiguration.builder()
                .nullReplacement("N/A")  // Default is empty string
                .build();

        Table maintenanceLogTable = Clique.table(TableType.BOX_DRAW, config)
                .headers(
                        "[*blue, bold]DEVICE NAME[/]",
                        "[*blue, bold]RACK NAME[/]",
                        "[*blue, bold]TYPE[/]",
                        "[*blue, bold]PERFORMED DATE[/]",
                        "[*blue, bold]NOTES[/]",
                        "[*blue, bold]FIRMWARE VERSION (BEFORE)[/]",
                        "[*blue, bold]FIRMWARE VERSION (AFTER)[/]",
                        "[*blue, bold]NEW EXPIRY DATE[/]"
                );
        maintenanceLogs.forEach(maintenanceLog -> maintenanceLogTable.row(maintenanceLog.getDeviceName(), maintenanceLog.getRackName(), maintenanceLog.getType().name(), maintenanceLog.getPerformedDate().toString(),
                maintenanceLog.getNotes(), maintenanceLog.getFirmwareVersionBefore(), maintenanceLog.getFirmwareVersionAfter(), (maintenanceLog.getNewExpiryDate() != null) ? maintenanceLog.getNewExpiryDate().toString() : "N/A"));
    maintenanceLogTable.render();
    }

    public void viewMaintenanceLogsByDevice(Device device) {
        List<MaintenanceLog> maintenanceLogs =  maintenanceLogRepository.findByDeviceId(device.getId());
        if(!maintenanceLogs.isEmpty()) {
            System.out.println("| LIST OF MAINTENANCE LOGS |");
            System.out.println("DEVICE: "  + device.getName());
            displayMaintenanceLogs(maintenanceLogs);
        }
        else
        {
            System.out.println("There are no maintenance logs for this device");
        }
    }

    public void deleteMaintenanceLog(MaintenanceLog maintenanceLog) {
        maintenanceLogRepository.delete(maintenanceLog);
    }
}
