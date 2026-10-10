package io.rackwatch.menu;

import io.rackwatch.authentication.SessionContext;
import io.rackwatch.enums.Role;
import io.rackwatch.interfaces.IMenu;
import io.rackwatch.service.UserService;
import io.rackwatch.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingsMenu implements IMenu {

    @Autowired
    private UserService userService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> changePassword();
                case 2 -> setAlertLeadDays();
                case 3 -> addUser();
                case 4 -> deleteUser();
            }
        }
        while (choice != 0);
    }

    public void deleteUser() {
        if(verifyAdmin()) {
            System.out.println("Enter the username of the user that you want to delete:");
            String username = InputHandler.getStringInput();
            System.out.println("Are you sure you want to delete this user? (Y/N):");
            String answer = InputHandler.getStringInput();
            if("Y".equalsIgnoreCase(answer)) {
                userService.deleteUser(username);
            }
        }
    }

    public void addUser() {
        if(verifyAdmin()) {
            System.out.println("Enter the full name:");
            String fullName = InputHandler.getStringInput();
            System.out.println("Enter the username:");
            String username = InputHandler.getStringInput();
            System.out.println("Enter the password:");
            String password = InputHandler.getStringInput();
            System.out.println("Enter the role (ADMINISTRATOR, USER):");
            Role role = Role.valueOf(InputHandler.getStringInput().toUpperCase());

            userService.addUser(fullName, username, password, role);
            System.out.println("Your user has been added!");
        }
    }

    public void setAlertLeadDays() {
        System.out .println("Enter the number of alert lead days:");
        int alertLeadDays = InputHandler.getIntegerInput();
        userService.setAlertLeadDays(alertLeadDays);
    }

    public void changePassword() {
        System.out .println("Enter the new password:");
        String newPassword = InputHandler.getStringInput();
        userService.changePassword(newPassword);
    }

    public void printOptions() {
        System.out.println();
        System.out.println("[1] Change password");
        System.out.println("[2] Set alert lead days");
        if(verifyAdmin()) {
            System.out.println("[3] Add user");
            System.out.println("[4] Delete user");
        }
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    private boolean verifyAdmin() {
        return Role.ADMINISTRATOR.equals(SessionContext.getUser().getRole());
    }

}
