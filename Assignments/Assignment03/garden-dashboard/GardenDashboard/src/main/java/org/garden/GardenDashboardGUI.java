package org.garden;

import javax.swing.*;
import java.awt.*;

public class GardenDashboardGUI {
    JLabel textAreaL = new JLabel("Luminosity: ");
    JLabel textAreaT = new JLabel("Temperature: ");
    JLabel textAreaLed1 = new JLabel("Led1: ");
    JLabel textAreaLed2 = new JLabel("Led2: ");
    JLabel textAreaLed3 = new JLabel("Led3: ");
    JLabel textAreaLed4 = new JLabel("Led4: ");
    JLabel textAreaIrrigation = new JLabel("Irrigation: ");
    public GardenDashboardGUI() {
        JFrame mainFrame = new JFrame("GardenDashboard");
        mainFrame.setSize(300, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        mainFrame.getContentPane().add(panel);
        panel.add(textAreaL);
        panel.add(textAreaT);
        panel.add(textAreaLed1);
        panel.add(textAreaLed2);
        panel.add(textAreaLed3);
        panel.add(textAreaLed4);
        panel.add(textAreaIrrigation);

        mainFrame.setVisible(true);
    }

    public void updateLuminosity(int value) {
        textAreaL.setText("Luminosity: " + value);
    }

    public void updateTemperature(int value) {
        textAreaT.setText("Temperature: " + value);
    }

    public void updateLed1(int value) {
        textAreaLed1.setText("Led1: " + value);
    }

    public void updateLed2(int value) {
        textAreaLed2.setText("Led2: " + value);
    }

    public void updateLed3(int value) {
        textAreaLed3.setText("Led3: " + value);
    }

    public void updateLed4(int value) {
        textAreaLed4.setText("Led4: " + value);
    }

    public void updateIrrigation(int value) {
        textAreaIrrigation.setText("Irrigation: " + value);
    }
}
