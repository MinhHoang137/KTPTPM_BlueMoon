package view.user;

import java.util.Scanner;

public class UserLoginView {
    private Scanner scanner = new Scanner(System.in);

    public String[] login() {
      

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        return new String[]{username, password};
    }
}