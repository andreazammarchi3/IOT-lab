package org.garden;

import javax.swing.*;

public class GardenDashboardGUI {
    JTextArea outputTextArea = new JTextArea();
    public GardenDashboardGUI() {

        outputTextArea.setEditable(false);
        outputTextArea.setAutoscrolls(true);
        outputTextArea.setText("Test\nn.1");

        JFrame mainFrame = new JFrame("GardenDashboard");
        mainFrame.setSize(300, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.getContentPane().add(outputTextArea);

        mainFrame.setVisible(true);
    }

    public void setOutputTextAreaText(String s) {
        outputTextArea.setText(s);
    }
}
