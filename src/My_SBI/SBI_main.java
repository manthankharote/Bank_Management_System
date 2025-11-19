package My_SBI;

import java.util.Scanner;

public class SBI_main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SBI_pd bankSystem = new SBI_pd();
        
        int outerChoice;

        do {
            System.out.println("\n=============================");
            System.out.println("   WELCOME TO SBI BANKING    ");
            System.out.println("=============================");
            System.out.println("1. Login");
            System.out.println("2. Register New Account");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            outerChoice = sc.nextInt();

            switch (outerChoice) {
                case 1: // LOGIN
                    boolean isLoggedIn = bankSystem.loginUser();
                    
                    if (isLoggedIn) {
                        // --- INNER MENU (Only shows after login) ---
                        int innerChoice;
                        do {
                            System.out.println("\n--- USER DASHBOARD ---");
                            System.out.println("1. Deposit Money");
                            System.out.println("2. Withdraw Money");
                            System.out.println("3. Check Balance");
                            System.out.println("4. Logout");
                            System.out.print("Select Operation: ");
                            innerChoice = sc.nextInt();

                            switch (innerChoice) {
                                case 1:
                                    bankSystem.depositMoney();
                                    break;
                                case 2:
                                    bankSystem.withdrawMoney();
                                    break;
                                case 3:
                                    bankSystem.checkBalance();
                                    break;
                                case 4:
                                    bankSystem.logout();
                                    break; // Breaks the switch, loop condition handles the exit
                                default:
                                    System.out.println("Invalid option.");
                            }
                        } while (innerChoice != 4); // Keep showing menu until Logout (4) is pressed
                    }
                    break;

                case 2: // REGISTER
                    bankSystem.registerUser();
                    break;

                case 0:
                    System.out.println("Exiting System. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (outerChoice != 0);
        
        sc.close();
    }
}