import java.sql.*;
import java.util.*;

public class FeesManager {

    // Add student to the database
    public void addStudent(String name, String rollNo, String course) {
        String query = "INSERT INTO students (name, roll_no, course) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, rollNo);
            stmt.setString(3, course);
            stmt.executeUpdate();
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add fee for a student
    public void addFee(int studentId, double amount, java.util.Date dueDate) {
        String query = "INSERT INTO fees (student_id, amount, due_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setDouble(2, amount);
            stmt.setDate(3, new java.sql.Date(dueDate.getTime()));
            stmt.executeUpdate();
            System.out.println("Fee added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Make a payment for a fee
    public void makePayment(int feeId, double paymentAmount) {
        String query = "SELECT amount FROM fees WHERE fee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, feeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double dueAmount = rs.getDouble("amount");
                if (paymentAmount <= dueAmount) {
                    // Update payments table
                    String paymentQuery = "INSERT INTO payments (fee_id, payment_date, amount) VALUES (?, ?, ?)";
                    try (PreparedStatement paymentStmt = conn.prepareStatement(paymentQuery)) {
                        paymentStmt.setInt(1, feeId);
                        paymentStmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                        paymentStmt.setDouble(3, paymentAmount);
                        paymentStmt.executeUpdate();
                        System.out.println("Payment made successfully.");

                        // Update fees table status
                        if (paymentAmount == dueAmount) {
                            String updateFeeQuery = "UPDATE fees SET status = 'Paid' WHERE fee_id = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateFeeQuery)) {
                                updateStmt.setInt(1, feeId);
                                updateStmt.executeUpdate();
                                System.out.println("Fee status updated to Paid.");
                            }
                        }
                    }
                } else {
                    System.out.println("Payment amount exceeds due amount.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // List all outstanding fees
    public void listOutstandingFees() {
        String query = "SELECT f.fee_id, s.name, f.amount, f.due_date, f.status FROM fees f " +
                       "JOIN students s ON f.student_id = s.student_id WHERE f.status = 'Unpaid'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("Fee ID: " + rs.getInt("fee_id") +
                                   ", Student Name: " + rs.getString("name") +
                                   ", Amount: " + rs.getDouble("amount") +
                                   ", Due Date: " + rs.getDate("due_date") +
                                   ", Status: " + rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
