package org.garden;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class GardenDashboardGUI {

    JLabel textAreaL = new JLabel("Luminosity: ");
    JLabel textAreaT = new JLabel("Temperature: ");
    JLabel textAreaLed1 = new JLabel("Led 1: ");
    JLabel textAreaLed2 = new JLabel("Led 2: ");
    JLabel textAreaLed3 = new JLabel("Led 3: ");
    JLabel textAreaLed4 = new JLabel("Led 4: ");
    JLabel textAreaIrrigation = new JLabel("Irrigation: ");
    JLabel textAreaMode = new JLabel("Mode: ");

    public GardenDashboardGUI() {
        JFrame mainFrame = new JFrame("GardenDashboard");
        mainFrame.setSize(200, 200);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    GardenDashboard.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
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
        panel.add(textAreaMode);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    public void updateLuminosity(int value) {
        textAreaL.setText("Luminosity: " + value + "/7");
    }

    public void updateTemperature(int value) {
        textAreaT.setText("Temperature: " + value + "/4");
    }

    public void updateIrrigation(int value) {
        textAreaIrrigation.setText("Irrigation: " + value);
    }

    public void updateMode(int value) {
        String mode;
        if (value == 0) {
            mode = "AUTO";
        } else if (value == 1) {
            mode = "MANUAL";
        } else {
            mode = "ALARM";
        }
        textAreaMode.setText("Mode: " + mode);
    }

    public void updateLed(int value, int led) {
        switch (led) {
            case 1 -> textAreaLed1.setText("Led 1: " + value + "/1");
            case 2 -> textAreaLed2.setText("Led 2: " + value + "/1");
            case 3 -> textAreaLed3.setText("Led 3: " + value + "/4");
            case 4 -> textAreaLed4.setText("Led 4: " + value + "/4");
        }

    }
}
