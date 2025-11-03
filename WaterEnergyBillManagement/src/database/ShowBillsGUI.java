package database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ShowBillsGUI {

    public static void main(String[] args) {
        // Create GUI Frame
        JFrame frame = new JFrame("Water and Energy Bills");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        // Table Columns
        String[] columns = {"ID", "Customer Name", "Type", "Amount", "Month", "Details"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Fetch data from database
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "❌ Failed to connect to database!");
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM bills ORDER BY id DESC");

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    rs.getString("bill_type"),
                    rs.getDouble("amount"),
                    rs.getString("billing_month"),
                    rs.getString("details")
                };
                model.addRow(row);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }

        // Set up JTable and scroll pane
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        // Show the frame
        frame.setVisible(true);
    }
}
