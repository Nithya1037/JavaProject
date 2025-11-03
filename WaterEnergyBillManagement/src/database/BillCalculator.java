package database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BillCalculator extends JFrame {
    private JTextField txtCustomerName, txtUnits;
    private JComboBox<String> cmbBillType;
    private JLabel lblResult;

    public BillCalculator() {
        setTitle("Water & Energy Bill Calculator");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(230, 255, 240));

        JLabel lblTitle = new JLabel("WATER & ENERGY BILL CALCULATION", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 15));
        lblTitle.setBounds(30, 20, 320, 25);
        add(lblTitle);

        JLabel lblName = new JLabel("Customer Name:");
        lblName.setBounds(50, 70, 120, 25);
        add(lblName);

        txtCustomerName = new JTextField();
        txtCustomerName.setBounds(180, 70, 150, 25);
        add(txtCustomerName);

        JLabel lblUnits = new JLabel("Units Consumed:");
        lblUnits.setBounds(50, 110, 120, 25);
        add(lblUnits);

        txtUnits = new JTextField();
        txtUnits.setBounds(180, 110, 150, 25);
        add(txtUnits);

        JLabel lblType = new JLabel("Bill Type:");
        lblType.setBounds(50, 150, 120, 25);
        add(lblType);

        cmbBillType = new JComboBox<>(new String[]{"Water", "Energy"});
        cmbBillType.setBounds(180, 150, 150, 25);
        add(cmbBillType);

        JButton btnCalculate = new JButton("Calculate & Save");
        btnCalculate.setBounds(110, 200, 160, 35);
        btnCalculate.setBackground(new Color(0, 153, 76));
        btnCalculate.setForeground(Color.WHITE);
        add(btnCalculate);

        lblResult = new JLabel("", SwingConstants.CENTER);
        lblResult.setBounds(30, 250, 320, 25);
        lblResult.setFont(new Font("Arial", Font.BOLD, 13));
        add(lblResult);

        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAndSaveBill();
            }
        });
    }

    private void calculateAndSaveBill() {
        String customerName = txtCustomerName.getText().trim();
        String unitsText = txtUnits.getText().trim();
        String billType = (String) cmbBillType.getSelectedItem();

        if (customerName.isEmpty() || unitsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double units = Double.parseDouble(unitsText);
            double rate = billType.equals("Water") ? 3.5 : 5.0;
            double amount = units * rate;

            lblResult.setText("Total Bill: ₹" + amount);

            saveBillToDatabase(customerName, amount, billType);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number for units.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveBillToDatabase(String customerName, double amount, String billType) {
        Connection conn = DBConnection.getConnection(); 

        if (conn == null) {
            JOptionPane.showMessageDialog(this, "❌ Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String sql = "INSERT INTO bills (customer_name, amount, bill_type) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, customerName);
            ps.setDouble(2, amount);
            ps.setString(3, billType);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Bill saved successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error saving bill: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception ignored) {}
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BillCalculator().setVisible(true));
    }
}
