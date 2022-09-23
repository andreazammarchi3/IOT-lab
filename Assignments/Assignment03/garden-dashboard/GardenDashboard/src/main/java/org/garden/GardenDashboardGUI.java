package org.garden;

import javax.swing.*;
import java.awt.*;

public class GardenDashboardGUI {
    JLabel textAreaL = new JLabel("Luminosity: ");
    JLabel textAreaT = new JLabel("Temperature: ");
    public GardenDashboardGUI() {
        JFrame mainFrame = new JFrame("GardenDashboard");
        mainFrame.setSize(300, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        mainFrame.getContentPane().add(panel);
        panel.add(textAreaL);
        panel.add(textAreaT);

        mainFrame.setVisible(true);
    }

    public void setValueL(int l) {
        textAreaL.setText("Luminosity: " + String.valueOf(l));
    }

    public void setValueT(int t) {
        textAreaT.setText("Temperature: " + String.valueOf(t));
    }
}
