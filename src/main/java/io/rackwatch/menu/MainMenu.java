package io.rackwatch.menu;

import io.rackwatch.alert.AlertManager;
import io.rackwatch.interfaces.IMenu;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainMenu implements IMenu {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private RackMenu rackMenu;
    @Autowired
    private DeviceMenu deviceMenu;
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
                //case 3 -> maintenanceMenu.show();
                //case 4 -> reportMenu.show();
                //case 5 -> alertMenu.show();
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
        /*List<AlertResult> alerts = alertManager.evaluate(
                deviceService.findActiveByUserId(SessionContext.getUser().getId()));
        long critical = alerts.stream().filter(a -> a.getSeverity() == AlertSeverity.CRITICAL).count();
        if (!alerts.isEmpty()) {
            System.out.printf("! %d critical · %d active alerts — see Alerts (option 5)%n", critical, alerts.size());
        }*/
    }

}