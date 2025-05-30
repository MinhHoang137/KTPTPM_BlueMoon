package view.user;

import java.util.Scanner;

public class HomePageView {
    private Scanner scanner = new Scanner(System.in);

    public void showHomePage(String username) {
        System.out.println("\n=== Welcome, " + username + " ===");
        System.out.println("Please choose an option:");
        System.out.println("1. Fee");
        System.out.println("2. Resident");
        System.out.println("3. Report");
        System.out.println("0. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                System.out.println(">> Navigating to Fee Management...");
                
                break;
            case 2:
                System.out.println(">> Navigating to Resident Management...");
                
                break;
            case 3:
                System.out.println(">> Navigating to Report Section...");
                
                break;
            case 0:
                System.out.println(">> Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                showHomePage(username); 
        }
    }
}
