package io.rackwatch.service;

import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import io.rackwatch.authentication.SessionContext;
import io.rackwatch.enums.*;
import io.rackwatch.model.*;
import io.rackwatch.repository.DeviceRepository;
import io.rackwatch.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
           int newEnd = startUnit + deviceHeight - 1;
           for (Device device : devices) {
               int existingEnd = device.getStartUnit() + device.getUnitHeight() - 1;
               if (startUnit <= existingEnd && newEnd >= device.getStartUnit()) {
                   System.out.println("A device is already in this rack location.");
                   valid = false;
                   break;
               }
           }
       }

        return valid;
    }

    public void addComputeDevice(Rack rack, String name, String manufacturer, String model, String serial, int startUnit, int deviceHeight, int powerWatts, DeviceStatus status,
                                    LocalDate purchaseDate, LocalDate warrantyExpiryDate, String firmwareVersion, LocalDate lastFirmwareUpdate, String licenseName, LocalDate licenseExpiry,
                                    String notes, String tags, MaintenanceType scheduledMaintenanceType, Integer maintenanceIntervalDays, LocalDate lastScheduledMaintenanceDate,
                                    BigDecimal purchasePrice, Integer expectedLifespanYears, ComputeRole computeRole, int cpuCores, int ram, int storage)
    {
        List<String> tagList = (tags != null) ? List.of(tags.split(",")) : new ArrayList<>();

        if(validateDevice(deviceHeight, powerWatts, purchaseDate, name, serial, tagList, expectedLifespanYears)){
            ComputeDevice computeDevice = new ComputeDevice(SessionContext.getUser().getId(), rack.getId(), name, manufacturer, model, serial, startUnit, deviceHeight, powerWatts,
                    status, purchaseDate, warrantyExpiryDate, firmwareVersion, lastFirmwareUpdate, null, licenseName, licenseExpiry, notes, tagList,
                    scheduledMaintenanceType, maintenanceIntervalDays, lastScheduledMaintenanceDate, purchasePrice, expectedLifespanYears, computeRole, cpuCores, ram, storage);
            deviceRepository.save(computeDevice);
            System.out.println("Added compute device successfully!");
        }
    }

    public void addNetworkDevice(Rack rack, String name, String manufacturer, String model, String serial, int startUnit, int deviceHeight, int powerWatts,
                                    DeviceStatus status, LocalDate purchaseDate, LocalDate warrantyExpiryDate, String firmwareVersion, LocalDate lastFirmwareUpdate, String licenseName,
                                    LocalDate licenseExpiry, String notes, String tags, MaintenanceType scheduledMaintenanceType, Integer maintenanceIntervalDays, LocalDate lastScheduledMaintenanceDate,
                                    BigDecimal purchasePrice, Integer expectedLifespanYears, NetworkRole networkRole, int portCount, int maxSpeed, boolean isManaged) {
        List<String> tagList = (tags != null) ? List.of(tags.split(",")) : new ArrayList<>();

        if(validateDevice(deviceHeight, powerWatts, purchaseDate, name, serial, tagList, expectedLifespanYears)){
            NetworkDevice networkDevice = new NetworkDevice(SessionContext.getUser().getId(), rack.getId(), name, manufacturer, model, serial, startUnit, deviceHeight, powerWatts,
                    status, purchaseDate, warrantyExpiryDate, firmwareVersion, lastFirmwareUpdate, null, licenseName, licenseExpiry, notes, tagList,
                    scheduledMaintenanceType, maintenanceIntervalDays, lastScheduledMaintenanceDate, purchasePrice, expectedLifespanYears, networkRole, portCount, maxSpeed, isManaged);
            deviceRepository.save(networkDevice);
            System.out.println("Added network device successfully!");
        }
    }

    public void addPowerDevice(Rack rack, String name, String manufacturer, String model, String serial, int startUnit, int deviceHeight, int powerWatts,
                               DeviceStatus status, LocalDate purchaseDate, LocalDate warrantyExpiryDate, String firmwareVersion, LocalDate lastFirmwareUpdate,
                               String licenseName, LocalDate licenseExpiry, String notes, String tags, MaintenanceType scheduledMaintenanceType, Integer maintenanceIntervalDays,
                               LocalDate lastScheduledMaintenanceDate, BigDecimal purchasePrice, Integer expectedLifespanYears, PowerRole powerRole, int outletCount, int capacityWatts) {
        List<String> tagList = (tags != null) ? List.of(tags.split(",")) : new ArrayList<>();

        if(validateDevice(deviceHeight, powerWatts, purchaseDate, name, serial, tagList, expectedLifespanYears)){
            PowerDevice powerDevice = new PowerDevice(SessionContext.getUser().getId(), rack.getId(), name, manufacturer, model, serial, startUnit, deviceHeight, powerWatts,
                    status, purchaseDate, warrantyExpiryDate, firmwareVersion, lastFirmwareUpdate, null, licenseName, licenseExpiry, notes, tagList,
                    scheduledMaintenanceType, maintenanceIntervalDays, lastScheduledMaintenanceDate, purchasePrice, expectedLifespanYears, powerRole, outletCount, capacityWatts);
            deviceRepository.save(powerDevice);
            System.out.println("Added power device successfully!");
        }
    }

    private boolean validateDevice(int deviceHeight, int powerWatts, LocalDate purchaseDate, String name, String serial, List<String> tags, Integer expectedLifespanYears) {
        boolean result = true;

        if (deviceHeight < 1) {
            System.out.println("The device height must be greater than zero.");
            result = false;
        }
        if (powerWatts < 1) {
            System.out.println("The power watts must be greater than zero.");
            result = false;
        }
        if (purchaseDate.isAfter(LocalDate.now())) {
            System.out.println("The purchase date cannot be in the future.");
            result = false;
        }
        if (!checkUniqueDeviceName(name)) {
            System.out.println("The device name must be unique.");
            result = false;
        }
        if ((serial != null) && !checkUniqueSerialNumber(serial)) {
            System.out.println("The serial number must be unique.");
            result = false;
        }
        if (!checkTags(tags)) {
            System.out.println("A device can have a maximum of 10 tags.");
            result = false;
        }
        if ((expectedLifespanYears != null) && ((expectedLifespanYears < 1) || (expectedLifespanYears > 30))) {
            System.out.println("The expected lifespan years must be between 1 and 30.");
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

    private boolean checkTags(List<String> tagList) {
        return (tagList.size() < 10) && tagList.stream()
                .map(String::trim)
                .allMatch(tag -> (!tag.isEmpty()) && (tag.length() <= 20));
    }

    public void searchDevice(int choice, String search) {
        List<Device> devices = deviceRepository.findByUserId(SessionContext.getUser().getId());
        List<Device> filteredDevices = switch (choice) {
            case 1 -> devices.stream()
                    .filter(d -> d.getName().equalsIgnoreCase(search))
                    .toList();
            case 2 -> devices.stream()
                    .filter(d -> d.getTags().stream().anyMatch(tag -> tag.equals(search)))
                    .toList();
            case 3 -> devices.stream()
                    .filter(d -> d.getModel().equalsIgnoreCase(search))
                    .toList();
            case 4 -> devices.stream()
                    .filter(d -> d.getSerialNumber().equalsIgnoreCase(search))
                    .toList();
            default -> devices;
        };

        displayDevices(filteredDevices);
    }

    public List<Device> getAllDevicesByStatus(DeviceStatus deviceStatus) {
        List<Device> devices = null;
        if(deviceStatus != null) {
            devices = deviceRepository.findByUserIdAndStatus(SessionContext.getUser().getId(), deviceStatus);
        }
        else
        {
            devices = deviceRepository.findByUserId(SessionContext.getUser().getId());
        }

        return devices;
    }

    public void deleteDevice(Device device) {
        deviceRepository.delete(device);
    }

    public void decommissionDevice(Device device) {
        device.setStatus(DeviceStatus.DECOMMISSIONED);
        deviceRepository.save(device);
    }

    public void moveDevice(Device device, Rack rack, int startUnit) {
        if(validatePlacement(startUnit, device.getUnitHeight(), rack)) {
            device.setRackId(rack.getId());
            device.setStartUnit(startUnit);
            deviceRepository.save(device);
            System.out.println("Device moved successfully!");
        }
    }

    public void viewDeviceDetails(Device device) {
        System.out.println("| DEVICE DETAILS |");
        System.out.println(device.getDetails());
    }

    public void editDevice(Device device, String tags, String notes, String licenseName, Integer firmwareIntervalOverride, int powerWatts, Integer expectedLifespanYears, BigDecimal purchasePrice, Integer maintenanceIntervalDays) {
        List<String> tagList = (tags != null) ? List.of(tags.split(",")) : new ArrayList<>();

        device.setNotes(notes);
        device.setLicenseName(licenseName);
        device.setFirmwareIntervalOverrideDays(firmwareIntervalOverride);
        device.setMaintenanceIntervalDays(maintenanceIntervalDays);
        device.setPowerWatts(powerWatts);
        device.setExpectedLifespanYears(expectedLifespanYears);
        device.setPurchasePrice(purchasePrice);
        device.setTags(tagList);
        deviceRepository.save(device);
    }
}
