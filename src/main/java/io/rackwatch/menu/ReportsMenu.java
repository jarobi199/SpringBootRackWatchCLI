package io.rackwatch.menu;

import io.rackwatch.enums.DeviceStatus;
import io.rackwatch.interfaces.IMenu;
import io.rackwatch.model.Device;
import io.rackwatch.service.ReportService;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportsMenu implements IMenu {

    @Autowired
    private ReportService reportService;
    @Autowired
    private DeviceMenu deviceMenu;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> rackUtilization();
                case 2 -> upcomingExpirations();
                case 3 -> firmwareStatus();
                case 4 -> maintenanceSummary();
                case 5 -> costAndDepreciation();
            }
        }
        while (choice != 0);
    }

    public void maintenanceSummary() {
        System.out.println("Please select one device or all devices:");
        System.out.println("[1] All devices");
        System.out.println("[2] Select device:");

        if (InputHandler.getIntegerInput() == 1) {
            reportService.maintenanceSummary(null);
        }
        else
        {
            Device device = deviceMenu.listAllDevicesByStatusAndSelect(DeviceStatus.ACTIVE);
            if (device != null) {
                reportService.maintenanceSummary(device);
            }
        }
    }

    public void costAndDepreciation() {
        reportService.costAndDepreciation();
    }

    public void firmwareStatus() {
        reportService.firmwareStatus();
    }

    public void upcomingExpirations() {
        reportService.generateUpcomingExpirations();
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
