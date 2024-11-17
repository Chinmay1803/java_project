import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class FeesManagementGUI {

    private JFrame frame;
    private FeesManager feesManager;

    public FeesManagementGUI() {
        feesManager = new FeesManager();
        frame = new JFrame("Fees Management System");

        // Create UI components
        JButton addStudentButton = new JButton("Add Student");
        JButton addFeeButton = new JButton("Add Fee");
        JButton makePaymentButton = new JButton("Make Payment");
        JButton listFeesButton = new JButton("List Outstanding Fees");

        addStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Enter Student Name:");
                String rollNo = JOptionPane.showInputDialog("Enter Roll Number:");
                String course = JOptionPane.showInputDialog("Enter Course:");
                feesManager.addStudent(name, rollNo, course);
            }
        });

        addFeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int studentId = Integer.parseInt(JOptionPane.showInputDialog("Enter Student ID:"));
                double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter Fee Amount:"));
                String dueDateStr = JOptionPane.showInputDialog("Enter Due Date (yyyy-mm-dd):");
                Date dueDate = java.sql.Date.valueOf(dueDateStr);
                feesManager.addFee(studentId, amount, dueDate);
            }
        });

        makePaymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int feeId = Integer.parseInt(JOptionPane.showInputDialog("Enter Fee ID:"));
                double paymentAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter Payment Amount:"));
                feesManager.makePayment(feeId, paymentAmount);
            }
        });

        listFeesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                feesManager.listOutstandingFees();
            }
        });

        // Layout the components
        frame.setLayout(new FlowLayout());
        frame.add(addStudentButton);
        frame.add(addFeeButton);
        frame.add(makePaymentButton);
        frame.add(listFeesButton);

        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FeesManagementGUI();
            }
        });
    }
}

