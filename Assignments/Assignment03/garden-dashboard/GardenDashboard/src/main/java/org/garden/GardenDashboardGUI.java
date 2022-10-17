package org.garden;

import javax.swing.*;
import java.awt.*;

public class GardenDashboardGUI {

    JLabel textAreaL = new JLabel("Luminosity: ");
    JLabel textAreaT = new JLabel("Temperature: ");
    JLabel textAreaOnOffLights = new JLabel("ON/OFF lights: ");
    JLabel textAreaFadeLights = new JLabel("Fading Lights: ");
    JLabel textAreaIrrigation = new JLabel("Irrigation: ");
    JLabel textAreaMode = new JLabel("Mode: ");

    public GardenDashboardGUI() {
        JFrame mainFrame = new JFrame("GardenDashboard");
        mainFrame.setSize(300, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        mainFrame.getContentPane().add(panel);
        panel.add(textAreaL);
        panel.add(textAreaT);
        panel.add(textAreaOnOffLights);
        panel.add(textAreaFadeLights);
        panel.add(textAreaIrrigation);
        panel.add(textAreaMode);

        mainFrame.setVisible(true);
    }

    public void updateLuminosity(int value) {
        textAreaL.setText("Luminosity: " + value + "/8");
    }

    public void updateTemperature(int value) {
        textAreaT.setText("Temperature: " + value + "/5");
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

    public void updateOnOffLights(boolean value) {
        textAreaOnOffLights.setText("ON/OFF Lights: " + value);
    }

    public void updateFadeLights(int value) {
        if (value == 0) {
            value = 5;
        }
        textAreaFadeLights.setText("Fading Lights: " + (6 - value) + "/5");
    }
}
