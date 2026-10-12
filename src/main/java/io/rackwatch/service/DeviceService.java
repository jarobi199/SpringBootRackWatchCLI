package io.rackwatch.service;

import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import io.rackwatch.authentication.SessionContext;
import io.rackwatch.enums.ComputeRole;
import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.enums.DeviceType;
import io.rackwatch.enums.MaintenanceType;
import io.rackwatch.model.Device;
import io.rackwatch.model.Rack;
import io.rackwatch.repository.DeviceRepository;
import io.rackwatch.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private RackRepository rackRepository;

    public boolean areDevicesAvailable() {
        return deviceRepository.count() > 0;
    }

    public void filterAndList(int choice, String value) {
        List<Device> devices = deviceRepository.findByUserId(SessionContext.getUser().getId());
        List<Device> filteredDevices = switch (choice) {
            case 2 -> devices.stream()
                    .filter(d -> d.getRackId().equalsIgnoreCase(value))
                    .toList();
            case 3 -> devices.stream()
                    .filter(d -> d.getDeviceType().equals(DeviceType.valueOf(value)))
                    .toList();
            case 4 -> devices.stream()
                    .filter(d -> d.getTags().stream().anyMatch(tag -> tag.equals(value)))
                    .toList();
            case 5 -> devices.stream()
                    .filter(d -> d.getStatus().equals(DeviceStatus.valueOf(value)))
                    .toList();
            default -> devices;
        };

        displayDevices(filteredDevices);
    }

    public void displayDevices(List<Device> devices) {
        System.out.println("| LIST OF DEVICES |");
        Table deviceTable = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[*blue, bold]NAME[/]",
                        "[*blue, bold]RACK[/]",
                        "[*blue, bold]TYPE[/]",
                        "[*blue, bold]MANUFACTURER[/]",
                        "[*blue, bold]MODEL[/]",
                        "[*blue, bold]UNITS[/]",
                        "[*blue, bold]POWER WATTS[/]",
                        "[*blue, bold]PURCHASE DATE[/]",
                        "[*blue, bold]STATUS[/]"
                );
        for (Device device : devices) {
            Rack rack = rackRepository.findById(device.getRackId()).orElse(null);
            deviceTable.row(device.getName(), (rack != null) ? rack.getName() : "N/A", device.getDeviceType().name(), device.getManufacturer(), device.getModel(), device.getUnitHeight().toString(),
                    device.getPowerWatts().toString(), device.getPurchaseDate().toString(), device.getStatus().name());
        }
        deviceTable.render();
    }

    public boolean validatePlacement(int startUnit, int deviceHeight, Rack rack) {
        boolean valid = true;
       if ((startUnit < 1) || ((startUnit + deviceHeight - 1) > rack.getTotalUnits())) {
           System.out.println("Invalid start unit or unit size");
            valid = false;
        }
       else
       {
           List<Device> devices = deviceRepository.findByRackId(rack.getId()).stream().filter(d -> DeviceStatus.ACTIVE.equals(d.getStatus())).toList();
           for(Device device : devices) {
               if ((startUnit >= device.getStartUnit()) || (startUnit + deviceHeight - 1) <= (device.getStartUnit() + device.getUnitHeight() - 1)) {
                   System.out.println("A device is already in this rack location.");
                   valid = false;
                   break;
               }
           }
       }

        return valid;

    }

    public boolean addComputeDevice(Rack rack,
                                 int startUnit,
                                 int deviceHeight,
                                 DeviceType deviceType,
                                 String name,
                                 String manufacturer,
                                 String model,
                                 String serial,
                                 int powerWatts,
                                 DeviceStatus status,
                                 LocalDate purchaseDate,
                                 LocalDate warrantyExpiryDate,
                                 String firmwareVersion,
                                 LocalDate lastFirmwareUpdate,
                                 String licenseName,
                                 LocalDate licenseExpiry,
                                 String notes,
                                 String tags,
                                 String scheduledMaintenanceTypeInput,
                                 MaintenanceType scheduledMaintenanceType,
                                 Integer maintenanceIntervalDays,
                                 LocalDate lastScheduledMaintenanceDate,
                                 String purchasePriceInput,
                                 BigDecimal purchasePrice,
                                 Integer expectedLifespanYears,
                                 ComputeRole computeRole,
                                 int cpuCores,
                                 int ram,
                                 int storage
    )
    {
        boolean result = true;

        //Validation
        if(deviceHeight < 1) {
            System.out.println("The device height must be greater than zero.");
            result = false;
        }
        else if(powerWatts < 1) {
            System.out.println("The power watts must be greater than zero.");
            result = false;
        }
        else if(purchaseDate.isAfter(LocalDate.now())) {
            System.out.println("The purchase date cannot be in the future.");
            result = false;
        }
        else if(!checkUniqueDeviceName(name)) {
            System.out.println("The device name must be unique.");
            result = false;
        }
        else if((serial != null) && !checkUniqueSerialNumber(serial)) {
            System.out.println("The serial number must be unique.");
            result = false;
        }

        return result;
    }

    private boolean checkUniqueDeviceName(String name) {
        List<String> deviceNames = deviceRepository.findByUserId(SessionContext.getUser().getId()).stream().map(Device::getName).toList();
        return !deviceNames.contains(name);
    }

    private boolean checkUniqueSerialNumber(String serialNumber) {
        List<String> serialNumbers = deviceRepository.findByUserId(SessionContext.getUser().getId()).stream().map(Device::getSerialNumber).toList();
        return !serialNumbers.contains(serialNumber);
    }


}
