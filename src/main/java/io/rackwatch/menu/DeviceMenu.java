package io.rackwatch.menu;

import io.rackwatch.enums.*;
import io.rackwatch.interfaces.IMenu;
import io.rackwatch.model.Device;
import io.rackwatch.model.Rack;
import io.rackwatch.service.DeviceService;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DeviceMenu implements IMenu {

    @Autowired
    private RackMenu rackMenu;
    @Autowired
    private DeviceService deviceService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> listFilterDevices();
                case 2 -> searchDevices();
                case 3 -> addDevice();
                case 7 -> decommissionDevice();
                case 8 -> deleteDevice();
            }
        }
        while (choice != 0);
    }

    public void decommissionDevice() {
        Device device = listAllDevicesByStatusAndSelect(DeviceStatus.ACTIVE);
        if (device != null) {
            deviceService.decommissionDevice(device);
            System.out.println("Device decommissioned successfully!");
        }
    }

    public void deleteDevice() {
        Device device = listAllDevicesByStatusAndSelect(null);
        if (device != null) {
            System.out.println("Are you sure you want to delete this device? (Y/N):");
            boolean confirmation = InputHandler.getBooleanInput();
            if (confirmation) {
                deviceService.deleteDevice(device);
                System.out.println("Device deleted successfully!");
            }
        }
    }

    public void addDevice() {
        Rack rack = rackMenu.listAllRacksAndSelect();
        if (rack != null) {
            int startUnit;
            int deviceHeight;

            do
            {
                System.out.println("Please enter the start unit:");
                startUnit = InputHandler.getIntegerInput();
                System.out.println("Please enter the device unit height:");
                deviceHeight = InputHandler.getIntegerInput();
            }
            while (!deviceService.validatePlacement(startUnit, deviceHeight, rack));

            System.out.println("Please enter the device type (COMPUTE, NETWORK, POWER):");
            DeviceType deviceType = DeviceType.valueOf(InputHandler.getStringInput().toUpperCase());
            System.out.println("Please enter the device name:");
            String name = InputHandler.getStringInput();
            System.out.println("Please enter the device manufacturer:");
            String manufacturer = InputHandler.getStringInput();
            System.out.println("Please enter the device model:");
            String model = InputHandler.getStringInput();
            System.out.println("Please enter the device serial number (optional):");
            String serial = InputHandler.getStringInputNullable();
            System.out.println("Please enter the device power watts:");
            int powerWatts = InputHandler.getIntegerInput();
            System.out.println("Please enter the status of the device (ACTIVE, DECOMMISSIONED):");
            DeviceStatus status = DeviceStatus.valueOf(InputHandler.getStringInput().toUpperCase());
            System.out.println("Please enter the purchase date of the device (YYYY-MM-DD):");
            LocalDate purchaseDate = InputHandler.getDateInput();
            System.out.println("Please enter the warranty expiry date of the device (optional) (YYYY-MM-DD):");
            LocalDate warrantyExpiryDate = InputHandler.getDateInputNullable();
            System.out.println("Please enter the firmware version of the device:");
            String firmwareVersion = InputHandler.getStringInput();
            System.out.println("Please enter the last firmware update of the device (optional) (YYYY-MM-DD):");
            LocalDate lastFirmwareUpdate = InputHandler.getDateInputNullable();
            System.out.println("Please enter the license name of the device (optional):");
            String licenseName = InputHandler.getStringInputNullable();
            System.out.println("Please enter the license expiry date of the device (optional) (YYYY-MM-DD):");
            LocalDate licenseExpiry = InputHandler.getDateInputNullable();
            System.out.println("Please enter the notes:");
            String notes = InputHandler.getStringInput();
            System.out.println("Please enter any comma separated tags (optional):");
            String tags = InputHandler.getStringInputNullable();
            System.out.println("Please enter the scheduled maintenance type (optional) (FIRMWARE_UPDATE, HARDWARE_REPAIR, PART_REPLACEMENT, CLEANING,\n" +
                    "    CONFIG_CHANGE, LICENSE_RENEWAL, WARRANTY_EXTENSION):");
            String scheduledMaintenanceTypeInput = InputHandler.getStringInputNullable();
            MaintenanceType scheduledMaintenanceType = (scheduledMaintenanceTypeInput != null) ? MaintenanceType.valueOf(scheduledMaintenanceTypeInput.toUpperCase()) : null;
            System.out.println("Please enter the maintenance interval days of the device (optional):");
            Integer maintenanceIntervalDays = InputHandler.getIntegerInputNullable();
            System.out.println("Please enter the last scheduled maintenance date of the device (optional) (YYYY-MM-DD):");
            LocalDate lastScheduledMaintenanceDate = InputHandler.getDateInputNullable();
            System.out.println("Please enter the purchase price of the device (optional):");
            String purchasePriceInput = InputHandler.getStringInputNullable();
            BigDecimal purchasePrice = (purchasePriceInput != null) ? BigDecimal.valueOf(Long.parseLong(purchasePriceInput)) : null;
            System.out.println("Please enter the expected lifespan in years of the device (optional):");
            Integer expectedLifespanYears = InputHandler.getIntegerInputNullable();

            switch (deviceType) {
                case COMPUTE -> {
                    System.out.println("Please enter the compute role (HYPERVISOR, NAS, GENERAL_SERVER):");
                    ComputeRole computeRole = ComputeRole.valueOf(InputHandler.getStringInput().toUpperCase());
                    System.out.println("Please enter the number of CPU cores:");
                    int cpuCores = InputHandler.getIntegerInput();
                    System.out.println("Please enter the RAM in GB:");
                    int ram = InputHandler.getIntegerInput();
                    System.out.println("Please enter the storage in TB:");
                    int storage = InputHandler.getIntegerInput();

                    deviceService.addComputeDevice(rack, name, manufacturer, model, serial, startUnit, deviceHeight, powerWatts, status, purchaseDate, warrantyExpiryDate,
                            firmwareVersion, lastFirmwareUpdate, licenseName, licenseExpiry, notes, tags, scheduledMaintenanceType, maintenanceIntervalDays,
                            lastScheduledMaintenanceDate, purchasePrice, expectedLifespanYears, computeRole, cpuCores, ram, storage);
                }
                 case NETWORK -> {
                     System.out.println("Please enter the network role (SWITCH, ROUTER, FIREWALL, ACCESS_POINT):");
                     NetworkRole networkRole = NetworkRole.valueOf(InputHandler.getStringInput().toUpperCase());
                     System.out.println("Please enter the port count:");
                     int portCount = InputHandler.getIntegerInput();
                     System.out.println("Please enter the max speed in Gbps:");
                     int maxSpeed = InputHandler.getIntegerInput();
                     System.out.println("Is this device managed? (Y/N):");
                     boolean isManaged = InputHandler.getBooleanInput();

                     deviceService.addNetworkDevice(rack, name, manufacturer, model, serial, startUnit, deviceHeight, powerWatts, status, purchaseDate, warrantyExpiryDate,
                             firmwareVersion, lastFirmwareUpdate, licenseName, licenseExpiry, notes, tags, scheduledMaintenanceType, maintenanceIntervalDays,
                             lastScheduledMaintenanceDate, purchasePrice, expectedLifespanYears, networkRole, portCount, maxSpeed, isManaged);
                 }
                case POWER -> {
                    System.out.println("Please enter the power role (PDU, UPS):");
                    PowerRole powerRole = PowerRole.valueOf(InputHandler.getStringInput().toUpperCase());
                    System.out.println("Please enter the outlet count:");
                    int outletCount = InputHandler.getIntegerInput();
                    System.out.println("Please enter the capacity in watts:");
                    int capacityWatts = InputHandler.getIntegerInput();

                    deviceService.addPowerDevice(rack, name, manufacturer, model, serial, startUnit, deviceHeight, powerWatts, status, purchaseDate, warrantyExpiryDate,
                            firmwareVersion, lastFirmwareUpdate, licenseName, licenseExpiry, notes, tags, scheduledMaintenanceType, maintenanceIntervalDays,
                            lastScheduledMaintenanceDate, purchasePrice, expectedLifespanYears, powerRole, outletCount, capacityWatts);
                }
            }
        }
    }

    public void searchDevices() {
        String value = null;
        System.out.println("Please select what you would like to search by:");
        System.out.println("[1] Name");
        System.out.println("[2] Tag");
        System.out.println("[3] Model");
        System.out.println("[4] Serial Number");
        System.out.println("[0] Back");
        int choice = InputHandler.getIntegerInput();
        System.out.println("Please enter your search value:");
        String search =  InputHandler.getStringInput();

        deviceService.searchDevice(choice, search);
    }

    public void listFilterDevices() {
        if(deviceService.areDevicesAvailable()) {
            String value = null;
            System.out.println("Please enter what you would want to filter on:");
            System.out.println("[1] No filter");
            System.out.println("[2] Rack");
            System.out.println("[3] Type");
            System.out.println("[4] Tag");
            System.out.println("[5] Status");
            System.out.println("[0] Back");
            int choice = InputHandler.getIntegerInput();

            switch (choice) {
                case 2 -> {
                    Rack rack = rackMenu.listAllRacksAndSelect();
                    if (rack != null) {
                        value = rack.getId();
                    }
                }
                case 3 -> {
                    System.out.println("Please enter the device type to filter (COMPUTE, NETWORK, POWER):");
                    value = InputHandler.getStringInput();
                }
                case 4 -> {
                    System.out.println("Please enter the name of the tag to filer on:");
                    value = InputHandler.getStringInput();
                }
                case 5 -> {
                    System.out.println("Please enter the device status to filer (ACTIVE, DECOMMISSIONED):");
                    value = InputHandler.getStringInput();
                }
            }

            deviceService.filterAndList(choice, value);
        }
        else
        {
            System.out.println("There are no devices available.");
        }
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] List/filter devices");
        System.out.println("[2] Search devices");
        System.out.println("[3] Add device");
        System.out.println("[4] View device detail");
        System.out.println("[5] Edit device");
        System.out.println("[6] Move device");
        System.out.println("[7] Decommission device");
        System.out.println("[8] Delete device");
        System.out.println("[0] Back");
        System.out.println("Please make a selection:");
    }

    public Device listAllDevicesByStatusAndSelect(DeviceStatus deviceStatus) {
        int number = 1;
        Device device = null;
        int choice;

        List<Device> devices = deviceService.getAllDevicesByStatus(deviceStatus);

        if(!devices.isEmpty()) {
            for (Device d : devices) {
                System.out.println("[" + number + "] " + d.getName());
                number++;
            }
            System.out.println("Select a device:");
            choice = InputHandler.getIntegerInput();
            device = devices.get(choice - 1);
        }
        else
        {
            System.out.println("There are no devices available.");
        }

        return device;
    }
}


