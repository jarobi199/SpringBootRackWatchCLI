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

            }
        }
        while (choice != 0);
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
