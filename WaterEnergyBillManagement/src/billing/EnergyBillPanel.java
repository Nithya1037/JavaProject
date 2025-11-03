package billing;

import javax.swing.*;
import java.awt.*;
import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EnergyBillPanel extends JPanel {
    private JTextField nameField, unitsField, monthField;

    public EnergyBillPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(255, 228, 196)); // light orange

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("⚡ Energy Bill");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Units Consumed:"), gbc);
        gbc.gridx = 1;
        unitsField = new JTextField(10);
        add(unitsField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Billing Month (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        monthField = new JTextField(10);
        add(monthField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JButton calcBtn = new JButton("Calculate & Save");
        JButton backBtn = new JButton("Back");
        JPanel btnPanel = new JPanel();
        btnPanel.add(calcBtn);
        btnPanel.add(backBtn);
        gbc.gridwidth = 2;
        add(btnPanel, gbc);

        // Actions
        calcBtn.addActionListener(e -> saveBill());
        backBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) {
                w.dispose();
            }
            new MainMenu().setVisible(true);
        });
    }

    private void saveBill() {
        String name = nameField.getText();
        String unitsText = unitsField.getText();
        String month = monthField.getText();

        if (name.isEmpty() || unitsText.isEmpty() || month.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double units = Double.parseDouble(unitsText);
            double amount = units * 15; // sample energy rate

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO bills (customer_name, bill_type, amount, billing_month, details) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, "ENERGY");
                ps.setDouble(3, amount);
                ps.setString(4, month);
                ps.setString(5, "Energy consumption bill");
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "✅ Energy bill saved successfully!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Invalid number format for units!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error saving bill!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

