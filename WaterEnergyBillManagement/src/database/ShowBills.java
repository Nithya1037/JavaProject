package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShowBills {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            System.out.println("❌ Database connection failed!");
            return;
        }

        System.out.println("\n💧 WATER AND ENERGY BILL RECORDS 💡\n");
        System.out.printf("%-5s %-15s %-10s %-10s %-12s %-30s%n",
                "ID", "Customer Name", "Type", "Amount", "Month", "Details");
        System.out.println("-------------------------------------------------------------------------------");

        try (Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM bills ORDER BY id DESC";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("customer_name");
                String type = rs.getString("bill_type");
                double amount = rs.getDouble("amount");
                String month = rs.getString("billing_month");
                String details = rs.getString("details");

                System.out.printf("%-5d %-15s %-10s %-10.2f %-12s %-30s%n",
                        id, name, type, amount, month, details);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
}
