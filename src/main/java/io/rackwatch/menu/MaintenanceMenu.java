package io.rackwatch.menu;

import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.interfaces.IMenu;
import io.rackwatch.model.Device;
import io.rackwatch.service.MaintenanceLogService;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMenu implements IMenu {

    @Autowired
    private MaintenanceLogService maintenanceLogService;
    @Autowired
    private DeviceMenu deviceMenu;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> logMaintenance();
            }
        }
        while (choice != 0);
    }

    public void logMaintenance() {
        Device device = deviceMenu.listAllDevicesByStatusAndSelect(DeviceStatus.ACTIVE);
        if (device != null) {
            //TODO: Add code here
        }
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] Log maintenance");
        System.out.println("[2] History by device");
        System.out.println("[3] View all logs");
        System.out.println("[4] Edit rack");
        System.out.println("[5] Delete log");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

}
