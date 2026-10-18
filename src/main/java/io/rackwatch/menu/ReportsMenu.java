package io.rackwatch.menu;

import io.rackwatch.interfaces.IMenu;
import io.rackwatch.service.ReportService;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportsMenu implements IMenu {

    @Autowired
    private ReportService reportService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> rackUtilization();
            }
        }
        while (choice != 0);
    }

    public void rackUtilization() {
        reportService.generateRackUtilization();
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] Rack utilization");
        System.out.println("[2] Upcoming expirations");
        System.out.println("[3] Firmware status");
        System.out.println("[4] Maintenance summary");
        System.out.println("[5] Cost & depreciation");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

}
