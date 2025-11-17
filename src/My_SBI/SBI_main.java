package My_SBI;

import java.util.*;

public class SBI_main {
    public static void main(String []args) {
        
        int  inputFromUser;
        Scanner input = new Scanner (System.in);
        
        SBI_pd bp = new SBI_pd();
        
        do {
            System.out.println("\n Welcome to My_SBI");
            System.out.println("Enter your choice:");
            System.out.println("0 Exit");
            System.out.println("1 New Registration");
            System.out.println("2 Deposit Money");
            System.out.println("3 withdraw Money");
            System.out.println("4 Check Account Balance / View All Customers"
            		+ "");
            inputFromUser = input.nextInt();
        
            switch(inputFromUser) {
                case 1:
                    bp.NewRegistration();
                    break;
                case 2:
                    bp.DepositMoney();
                    break;
                case 3:
                    bp.withdrawMoney();
                    break;
                case 4:
                    bp.CheckAcBal();
                    break;
                case 0:
                    System.out.println("Thank you for using My_SBI. Goodbye");
                    break;
                default:
                    System.out.println("Enter a correct Number -> ");
            }
        
        } while(inputFromUser != 0);
        
        input.close();
    }
}