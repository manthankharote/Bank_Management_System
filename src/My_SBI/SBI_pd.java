package My_SBI;

import java.sql.*;
import java.util.Scanner;
import java.util.Random;

public class SBI_pd {

    String url = "jdbc:mysql://localhost:3306/BANK_SBI";
    String user = "root";
    String password = "root";
    Scanner sc = new Scanner(System.in);

    // This variable stores the Account Number of the currently logged-in user
    private long loggedInAccount = -1; 

    // --- 1. REGISTRATION ---
    public void registerUser() {
        System.out.println("\n--- New Customer Registration ---");

        System.out.print("Enter Your Name: ");
        String name = sc.next(); // Using next() for single word or nextLine() for full name
        // sc.nextLine(); // Uncomment if you switch to nextLine() above

        System.out.print("Enter Mobile Number: ");
        String mobile = sc.next();

        System.out.print("Set a 6-Digit PIN: ");
        int pin = sc.nextInt();

        System.out.print("Enter Initial Deposit Amount: ");
        double balance = sc.nextDouble();

        // Auto-generate a random 10-digit Account Number
        long accountNo = (long) (Math.random() * 1000000000L) + 1000000000L;

        String sql = "INSERT INTO customer (accountNo, name, balance, mobile, pin) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, accountNo);
            pstmt.setString(2, name);
            pstmt.setDouble(3, balance);
            pstmt.setString(4, mobile);
            pstmt.setInt(5, pin);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Registration Successful!");
                System.out.println("---------------------------------");
                System.out.println("Your System Generated Account No is: " + accountNo);
                System.out.println("PLEASE NOTE THIS NUMBER FOR LOGIN.");
                System.out.println("---------------------------------");
            } else {
                System.out.println("Registration Failed.");
            }

        } catch (SQLException e) {
            System.out.println("Db Error: " + e.getMessage());
        }
    }

    // --- 2. LOGIN ---
    public boolean loginUser() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter Account Number: ");
        long accNo = sc.nextLong();
        System.out.print("Enter 6-Digit PIN: ");
        int pin = sc.nextInt();

        String sql = "SELECT * FROM customer WHERE accountNo = ? AND pin = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, accNo);
            pstmt.setInt(2, pin);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Login Success! Store the account number in our variable
                this.loggedInAccount = accNo;
                String name = rs.getString("name");
                System.out.println("\nLogin Successful! Welcome, " + name);
                return true;
            } else {
                System.out.println("Invalid Account Number or PIN.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
            return false;
        }
    }

    // --- 3. DEPOSIT (Uses loggedInAccount) ---
    public void depositMoney() {
        if (loggedInAccount == -1) {
            System.out.println("Please login first.");
            return;
        }

        System.out.print("Enter amount to deposit: ");
        double amount = sc.nextDouble();

        String sqlSelect = "SELECT balance FROM customer WHERE accountNo = ?";
        String sqlUpdate = "UPDATE customer SET balance = ? WHERE accountNo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
             PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {

            // Get current balance
            psSelect.setLong(1, loggedInAccount);
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                double currentBal = rs.getDouble("balance");
                double newBal = currentBal + amount;

                // Update balance
                psUpdate.setDouble(1, newBal);
                psUpdate.setLong(2, loggedInAccount);
                psUpdate.executeUpdate();

                System.out.println("Deposit Successful. New Balance: " + newBal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- 4. WITHDRAW (Uses loggedInAccount) ---
    public void withdrawMoney() {
        if (loggedInAccount == -1) {
            System.out.println("Please login first.");
            return;
        }

        System.out.print("Enter amount to withdraw: ");
        double amount = sc.nextDouble();

        String sqlSelect = "SELECT balance FROM customer WHERE accountNo = ?";
        String sqlUpdate = "UPDATE customer SET balance = ? WHERE accountNo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
             PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {

            psSelect.setLong(1, loggedInAccount);
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                double currentBal = rs.getDouble("balance");
                
                if (currentBal >= amount) {
                    double newBal = currentBal - amount;

                    psUpdate.setDouble(1, newBal);
                    psUpdate.setLong(2, loggedInAccount);
                    psUpdate.executeUpdate();
                    
                    System.out.println("Withdrawal Successful. Remaining Balance: " + newBal);
                } else {
                    System.out.println("Insufficient Balance! Your balance is: " + currentBal);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- 5. CHECK BALANCE (Uses loggedInAccount) ---
    public void checkBalance() {
        if (loggedInAccount == -1) {
            System.out.println("Please login first.");
            return;
        }

        String sql = "SELECT * FROM customer WHERE accountNo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, loggedInAccount);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n--- Account Details ---");
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Account No: " + rs.getLong("accountNo"));
                System.out.println("Mobile: " + rs.getString("mobile"));
                System.out.println("Current Balance: " + rs.getDouble("balance"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Helper to logout
    public void logout() {
        this.loggedInAccount = -1;
        System.out.println("Logged out successfully.");
    }
}