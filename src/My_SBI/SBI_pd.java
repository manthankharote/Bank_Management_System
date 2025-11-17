package My_SBI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SBI_pd {

   
    String url = "jdbc:mysql://localhost:3306/BANK_SBI";
    String user = "root";
    String password = "root";
    Scanner sc = new Scanner(System.in);



    public void NewRegistration() {
        System.out.println("New Registration....");
        

        String sql = "INSERT INTO customer (accountNo, name, balance) VALUES (?, ?, ?)";


        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            System.out.print("Enter New Account Number: ");
            int accountNo = sc.nextInt();

            System.out.print("Enter New Name of Customer: ");
            String name = sc.next();
            sc.nextLine(); 
            

            System.out.print("Enter initial balance: ");
            double balance = sc.nextDouble();

            preparedStatement.setInt(1, accountNo);
            preparedStatement.setString(2, name);
            preparedStatement.setDouble(3, balance);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Account Created Successfully..");
            } else {
                System.out.println("Failed Database issued.");
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
   
    }

    public void DepositMoney() {
        System.out.println("--- Credit Money ---");
        
        try {
            System.out.print("Enter account number: ");
            int accNo = sc.nextInt();
            System.out.print("Enter amount to deposit: ");
            double amount = sc.nextDouble();

           
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                
                String checkQuery = "SELECT balance FROM customer WHERE accountNo = ?";
                double currentBalance = -1;

                
                try (PreparedStatement psCheck = conn.prepareStatement(checkQuery)) {
                    psCheck.setInt(1, accNo);
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next()) {
                            currentBalance = rs.getDouble("balance");
                        }
                    }
                }

                if (currentBalance != -1) {
                    double newBalance = currentBalance + amount;
                    
                    String updateQuery = "UPDATE customer SET balance = ? WHERE accountNo = ?"; 
                    
                   
                    try (PreparedStatement psUpdate = conn.prepareStatement(updateQuery)) {
                        psUpdate.setDouble(1, newBalance);
                        psUpdate.setInt(2, accNo);
                        int rowsUpdated = psUpdate.executeUpdate();

                        if (rowsUpdated > 0) {
                            System.out.println("Amount credited successfully!");
                            System.out.println("Updated Balance: " + newBalance);
                        } else {
                            System.out.println("Failed to update balance!");
                        }
                    }
                } else {
                    System.out.println("Account not found!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during credit operation!");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Invalid input. Please enter numbers.");
            sc.nextLine(); 
        }
    }
	
	    public void withdrawMoney() {
	    	
	    }
	
	    public void CheckAcBal() {
    	
    }
}