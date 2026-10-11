package io.rackwatch.menu;

import io.rackwatch.interfaces.IMenu;
import io.rackwatch.model.Rack;
import io.rackwatch.service.RackService;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RackMenu implements IMenu {

    @Autowired
    private RackService rackService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> listAllRacks();
                case 2 -> addRack();
                case 3 -> editRack();
                case 4 -> deleteRack();
            }
        }
        while (choice != 0);
    }

    public void deleteRack() {
        Rack rack = listAllRacksAndSelect();
        if (rack != null) {
            if(rackService.hasDependencies(rack)) {
                System.out.println("This rack has dependencies and could not be deleted.");
            }
            else
            {
                rackService.deleteRack(rack);
            }
        }
    }

    public void editRack() {
        Rack rack = listAllRacksAndSelect();
        if (rack != null) {
            System.out.println("Enter the new rack name:");
            String rackName = InputHandler.getStringInput();
            System.out.println("Enter the new rack location:");
            String rackLocation = InputHandler.getStringInput();
            System.out.println("Enter the rack max power:");
            int rackMaxPowerWatts = InputHandler.getIntegerInput();

            rackService.editRack(rack, rackName, rackLocation, rackMaxPowerWatts);
            System.out.println("The rack edited successfully");
        }
    }

    public void addRack() {
        System.out.println("Enter the rack name:");
        String rackName = InputHandler.getStringInput();
        System.out.println("Enter the rack location:");
        String rackLocation = InputHandler.getStringInput();
        System.out.println("Enter the total units of the rack:");
        int rackTotalUnits = InputHandler.getIntegerInput();
        System.out.println("Enter the rack max power:");
        int rackMaxPowerWatts = InputHandler.getIntegerInput();

        rackService.addRack(rackName, rackLocation, rackTotalUnits, rackMaxPowerWatts);
        System.out.println("The rack added successfully!");
    }

    public void listAllRacks() {
        rackService.listAllRacks();
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] List all racks");
        System.out.println("[2] Add rack");
        System.out.println("[3] View rack elevation");
        System.out.println("[4]  Edit rack");
        System.out.println("[5]  Delete rack");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    public Rack listAllRacksAndSelect() {
        int number = 1;
        Rack rack = null;
        int choice;

        List<Rack> racks = rackService.getAllRacks();

        if(!racks.isEmpty()) {
            for (Rack r : racks) {
                System.out.println("[" + number + "] " +  r.getName() + " (" + r.getLocation() + ")");
                number++;
            }
            System.out.println("Select a rack:");
            choice = InputHandler.getIntegerInput();
            rack = racks.get(choice - 1);
        }
        else
        {
            System.out.println("There are no racks available.");
        }

        return rack;
    }

}
