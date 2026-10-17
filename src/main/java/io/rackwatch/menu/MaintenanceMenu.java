package io.rackwatch.menu;

import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.enums.MaintenanceType;
import io.rackwatch.interfaces.IMenu;
import io.rackwatch.model.Device;
import io.rackwatch.model.MaintenanceLog;
import io.rackwatch.service.DeviceService;
import io.rackwatch.service.MaintenanceLogService;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MaintenanceMenu implements IMenu {

    @Autowired
    private MaintenanceLogService maintenanceLogService;
    @Autowired
    private DeviceMenu deviceMenu;
    @Autowired
    DeviceService deviceService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> logMaintenance();
                case 2 -> historyByDevice();
                case 3 -> viewAllMaintenanceLogs();
                case 4 -> deleteMaintenanceLog();
            }
        }
        while (choice != 0);
    }

    public void deleteMaintenanceLog() {
        Device device = deviceMenu.listAllDevicesByStatusAndSelect(null);
        if (device != null) {
            MaintenanceLog maintenanceLog = listAllMaintenanceLogsByDeviceAndSelect(device);
            if(maintenanceLog != null) {
                maintenanceLogService.deleteMaintenanceLog(maintenanceLog);
                System.out.println("The maintenance log has been deleted successfully!");
            }
        }
    }

    public void historyByDevice() {
        Device device = deviceMenu.listAllDevicesByStatusAndSelect(null);
        if (device != null) {
            maintenanceLogService.viewMaintenanceLogsByDevice(device);
        }
    }

    public void viewAllMaintenanceLogs() {
        maintenanceLogService.viewAllMaintenanceLogs();
    }

    public void logMaintenance() {
        Device device = deviceMenu.listAllDevicesByStatusAndSelect(DeviceStatus.ACTIVE);
        String firmwareVersionAfter = null;
        LocalDate newExpiryDate = null;

        if (device != null) {
            System.out.println("Please enter the notes:");
            String notes = InputHandler.getStringInput();
            System.out.println("Enter the maintenance type ( FIRMWARE_UPDATE, HARDWARE_REPAIR, PART_REPLACEMENT, CLEANING,\n" +
                    "    CONFIG_CHANGE, LICENSE_RENEWAL, WARRANTY_EXTENSION):");
            MaintenanceType type = MaintenanceType.valueOf(InputHandler.getStringInput().toUpperCase());
            System.out.println("Please enter the date that the maintenance was performed (YYYY-MM-DD):");
            LocalDate performedDate = InputHandler.getDateInput();

            switch (type) {
                case FIRMWARE_UPDATE -> {
                    System.out.println("Please enter the updated firmware version:");
                    firmwareVersionAfter = InputHandler.getStringInput();
                }
                case LICENSE_RENEWAL, WARRANTY_EXTENSION -> {
                    System.out.println("Please enter the new expiry date (YYYY-MM-DD):");
                    newExpiryDate = InputHandler.getDateInput();
                }
            }

            maintenanceLogService.addMaintenanceLog(device, notes, type, performedDate, firmwareVersionAfter, newExpiryDate);
            deviceService.updateDevice(device, type, firmwareVersionAfter, newExpiryDate, performedDate);

            System.out.println("The maintenance log has been successfully added!");
        }
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] Log maintenance");
        System.out.println("[2] History by device");
        System.out.println("[3] View all logs");
        System.out.println("[4] Delete log");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    public MaintenanceLog listAllMaintenanceLogsByDeviceAndSelect(Device device) {
        int number = 1;
        MaintenanceLog maintenanceLog = null;
        int choice;

        List<MaintenanceLog> maintenanceLogs = maintenanceLogService.getAllMaintenanceLogsByDevice(device);

        if(!maintenanceLogs.isEmpty()) {
            for (MaintenanceLog m : maintenanceLogs) {
                System.out.println("[" + number + "] " + m.getNotes()  + " - " + m.getPerformedDate() + " (" + m.getType() + ")");
                number++;
            }
            System.out.println("Select a maintenance log:");
            choice = InputHandler.getIntegerInput();
            maintenanceLog = maintenanceLogs.get(choice - 1);
        }
        else
        {
            System.out.println("There are no maintenance logs available.");
        }

        return maintenanceLog;
    }
}
