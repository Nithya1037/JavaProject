package database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class BillCalculatorGUI extends JFrame {
    private JTextField nameField, unitsField;
    private JComboBox<String> billTypeBox;
    private JLabel totalLabel;
    private JTable billTable;
    private DefaultTableModel tableModel;

    // 🔹 Constructor to open directly for a specific bill type (e.g., "Energy" or "Water")
    public BillCalculatorGUI(String billType) {
        this(); // Call the default constructor
        billTypeBox.setSelectedItem(billType.toUpperCase());
    }

    // 🔹 Default constructor (builds the full GUI)
    public BillCalculatorGUI() {
        setTitle("💧 Water & Energy Bill Calculator");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 Top Panel for inputs
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        inputPanel.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Units Used:"));
        unitsField = new JTextField();
        inputPanel.add(unitsField);

        inputPanel.add(new JLabel("Bill Type:"));
        billTypeBox = new JComboBox<>(new String[]{"WATER", "ENERGY"});
        inputPanel.add(billTypeBox);

        JButton saveBtn = new JButton("Calculate & Save");
        saveBtn.addActionListener(e -> calculateAndSave());
        inputPanel.add(saveBtn);

        totalLabel = new JLabel("Total Bill: ₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(totalLabel);

        add(inputPanel, BorderLayout.NORTH);

        // 🔹 Table for showing saved records
        tableModel = new DefaultTableModel(new String[]{
            "ID", "Customer Name", "Type", "Amount", "Month", "Details"
        }, 0);
        billTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(billTable);
        add(scrollPane, BorderLayout.CENTER);

        // 🔹 Load existing bills from DB on startup
        loadBillsFromDB();
    }

    // 🔹 Method to calculate bill and save to database
    private void calculateAndSave() {
        String name = nameField.getText().trim();
        String billType = (String) billTypeBox.getSelectedItem();
        String unitsText = unitsField.getText().trim();

        if (name.isEmpty() || unitsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            double units = Double.parseDouble(unitsText);
            double amount = billType.equals("WATER") ? units * 55 : units * 85;
            totalLabel.setText("Total Bill: ₹" + amount);

            Connection conn = DBConnection.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "❌ Database connection failed!");
                return;
            }

            String query = "INSERT INTO bills (customer_name, bill_type, units, amount, bill_month, details) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, billType);
            stmt.setDouble(3, units);
            stmt.setDouble(4, amount);
            stmt.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setString(6, billType.equals("WATER") ? "Water usage bill" : "Energy consumption bill");
            stmt.executeUpdate();

            conn.close();
            JOptionPane.showMessageDialog(this, "✅ Bill saved successfully!");
            loadBillsFromDB(); // Refresh the table
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // 🔹 Load existing bills into the JTable
    private void loadBillsFromDB() {
        tableModel.setRowCount(0); // Clear old data
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM bills")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    rs.getString("bill_type"),
                    rs.getDouble("amount"),
                    rs.getDate("bill_month"),
                    rs.getString("details")
                });
            }
        } catch (Exception e) {
            System.out.println("Error loading bills: " + e.getMessage());
        }
    }

    // 🔹 Main method to run directly
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BillCalculatorGUI().setVisible(true));
    }
}
