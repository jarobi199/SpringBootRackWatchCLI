package io.rackwatch.menu;

import io.rackwatch.enums.AlertType;
import io.rackwatch.interfaces.IMenu;
import io.rackwatch.service.AlertService;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AlertsMenu implements IMenu {

    @Autowired
    private AlertService alertService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> viewAllAlerts();
                case 2 -> filterByAlertType();
            }
        }
        while (choice != 0);
    }

    public void filterByAlertType() {
        int choice;
        int number = 1;
        List<AlertType> alertTypes = Arrays.asList(AlertType.values());

        for(AlertType a : alertTypes) {
            System.out.println("[" + number + "] " + a.getDisplayName());
            number++;
        }

        System.out.println("Select an alert type:");
        choice = InputHandler.getIntegerInput();
        AlertType alertType = alertTypes.get(choice - 1);

        if(alertType != null) {
            alertService.displayAlerts(alertType);
        }
    }

    public void viewAllAlerts() {
        alertService.displayAlerts(null);
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] View all alerts");
        System.out.println("[2] Filter by alert type");
        System.out.println("Please make a selection:");
    }


}
