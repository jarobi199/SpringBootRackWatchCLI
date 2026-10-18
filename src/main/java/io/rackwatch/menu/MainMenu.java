package io.rackwatch.menu;

import io.rackwatch.alert.AlertManager;
import io.rackwatch.alert.AlertResult;
import io.rackwatch.enums.AlertSeverity;
import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.interfaces.IMenu;
import io.rackwatch.service.DeviceService;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainMenu implements IMenu {
    @Autowired
    DeviceService deviceService;
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private AlertsMenu alertMenu;
    @Autowired
    private RackMenu rackMenu;
    @Autowired
    private DeviceMenu deviceMenu;
    @Autowired
    private MaintenanceMenu maintenanceMenu;
    @Autowired
    private AuthenticateMenu authenticateMenu;
    @Autowired
    private SettingsMenu settingsMenu;
    @Autowired
    private GoodbyeMenu goodbyeMenu;

    @Override
    public void show() {
        int choice;
        IMenu menu;

        System.out.println();
        displayTitle();
        authenticateMenu.automaticLogin();
        System.out.println();

        do {
            displayAlertHeader();
            printOptions();
            choice = InputHandler.getIntegerInput();
            menu = switch (choice) {
                case 1 -> rackMenu;
                case 2 -> deviceMenu;
                case 3 -> maintenanceMenu;
                //case 4 -> reportMenu
                case 5 -> alertMenu;
                case 6 -> settingsMenu;
                case 0 -> goodbyeMenu;
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            };
            menu.show();
        }
        while (choice != 0);
    }

    public void printOptions() {
        System.out.println("[1] Racks");
        System.out.println("[2] Devices");
        System.out.println("[3] Maintenance");
        System.out.println("[4] Reports");
        System.out.println("[5] Alerts");
        System.out.println("[6] Settings");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    public void displayTitle() {
        System.out.println("============================================================================");
        System.out.println("     Welcome to the RackWatch Home Lab / Server Rack Manager Application! ");
        System.out.println("============================================================================");
    }

    private void displayAlertHeader() {
        List<AlertResult> alerts = alertManager.evaluate(
                deviceService.getAllDevicesByStatus(DeviceStatus.ACTIVE));
        long critical = alerts.stream().filter(a -> a.severity() == AlertSeverity.CRITICAL).count();
        if (!alerts.isEmpty()) {
            System.out.println(AlertManager.ANSI_RED +  "NOTE: THERE ARE " + critical + " CRITICAL ALERT(S)! See option #5 on the Alerts menu." + AlertManager.ANSI_RESET  + "\n");
        }
    }

}