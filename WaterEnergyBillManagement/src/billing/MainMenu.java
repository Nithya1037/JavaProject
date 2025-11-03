package billing;

import javax.swing.*;
import java.awt.*;
import database.BillCalculatorGUI; // ✅ import the GUI version

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Main Menu - Water & Energy Bill System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(46, 204, 113));

        JLabel title = new JLabel("Select Bill Module");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(140, 40, 300, 40);
        add(title);

        JButton waterBtn = new JButton("Water Bill");
        JButton energyBtn = new JButton("Energy Bill");
        JButton exitBtn = new JButton("Exit");

        waterBtn.setBounds(100, 130, 120, 50);
        energyBtn.setBounds(260, 130, 120, 50);
        exitBtn.setBounds(180, 220, 120, 50);

        add(waterBtn);
        add(energyBtn);
        add(exitBtn);

        // ✅ When "Water Bill" is clicked
        waterBtn.addActionListener(e -> {
            BillCalculatorGUI gui = new BillCalculatorGUI("Water");
            gui.setVisible(true);
            dispose();
        });

        // ✅ When "Energy Bill" is clicked
        energyBtn.addActionListener(e -> {
            BillCalculatorGUI gui = new BillCalculatorGUI("Energy");
            gui.setVisible(true);
            dispose();
        });

        exitBtn.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }
}
