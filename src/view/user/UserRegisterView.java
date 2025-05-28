package view.user;

import entity.user.User;

import java.util.Scanner;

public class UserRegisterView {
    private Scanner scanner = new Scanner(System.in);

    public User registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter role (admin/user): ");
        String role = scanner.nextLine();

        User newUser = new User(0, username, password, role);
        System.out.println("User registered successfully!");
        return newUser;
    }
}
