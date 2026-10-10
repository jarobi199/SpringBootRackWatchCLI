package io.rackwatch.menu;

import io.rackwatch.interfaces.IMenu;
import io.rackwatch.service.DeviceService;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DeviceMenu implements IMenu {

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
            }
        }
        while (choice != 0);
    }

    public void listFilterDevices() {
        String value = null;
        System.out.println("Please enter what you would want to filter on:");
        System.out.println("[1] No filter");
        System.out.println("[2] Rack");
        System.out.println("[3] Type");
        System.out.println("[4] Tag");
        System.out.println("[5] Status");
        System.out.println("[0] Back");
        int choice = InputHandler.getIntegerInput();

        deviceService.filterAndList(choice, value);
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] List / filter devices");
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

}


