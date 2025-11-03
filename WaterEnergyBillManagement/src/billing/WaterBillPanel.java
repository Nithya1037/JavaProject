package billing;

import javax.swing.*;
import java.awt.*;
import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class WaterBillPanel extends JPanel {
    private JTextField nameField, unitsField, monthField;

    public WaterBillPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(173, 216, 230)); // light blue

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("💧 Water Bill");
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
        double units = Double.parseDouble(unitsField.getText());
        String month = monthField.getText();
        double amount = units * 10; // sample water rate

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO bills (customer_name, bill_type, amount, billing_month, details) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, "WATER");
            ps.setDouble(3, amount);
            ps.setString(4, month);
            ps.setString(5, "Water usage bill");
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Water bill saved successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error saving bill!");
        }
    }
}

