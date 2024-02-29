/**
 * Made BY: Mihir Patel
 * Date: 2023-10-01
 * Description: Online grocery shopping.
 * Version: 1.0
 */
package GroceryStore;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        User u = new User();
        int choice = 0;
        System.out.println("++++++++++  WELCOME TO THE ONLINE GROCERY STORE  ++++++++++");
        do {

            try {
                // Display the main menu and accept user's choice
                Main.displayMainMenu();
                choice = Integer.parseInt(sc.nextLine());
            } catch (InputMismatchException e) {
                // Handle invalid input
                System.out.println("Invalid Input...");
            } catch (NumberFormatException e) {
                // Handle number format exceptions
            }

            // Perform actions based on user's choice
            switch (choice) {
                case 1:
                    u.searchByItem(); // Search for items by name
                    break;
                case 2:
                    u.searchByCategory(); // Search for items by category
                    break;
                case 3:
                    u.signUp(); // User sign-up
                    break;
                case 4:
                    u.signIn(); // User sign-in
                    break;
                case 5:
                    System.out.println("Exiting....");
                    break;
                default:
                    System.out.println("Invalid Input...");
                    break;
            }
        } while (choice != 5);
    }

    static void displayMainMenu() {
        System.out.println("________________________________________");
        System.out.println("1)Search By What You Want");
        System.out.println("2)Search By Category");
        System.out.println("3)Signup");
        System.out.println("4)Signin");
        System.out.println("5)Exit");
        System.out.println("________________________________________");
    }
    
}