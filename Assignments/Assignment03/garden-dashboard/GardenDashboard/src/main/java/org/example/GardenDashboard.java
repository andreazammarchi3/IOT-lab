package org.example;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class GardenDashboard {

    private static int temperature = 0;
    private static int luminosity = 0;
    private static GardenDashboardGUI GUI = new GardenDashboardGUI();

    public static void main(String[] args) throws Exception {
        // Start GUI
        JFrame frame = new JFrame("Garden Dashboard");
        frame.setContentPane(GUI.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Create client socket
        Socket s = new Socket("localhost", 888);

        // to read data coming from the server
        BufferedReader br
                = new BufferedReader(
                new InputStreamReader(
                        s.getInputStream()));

        String inputStr;
        while(temperature != -1 && luminosity != -1) {
            inputStr = br.readLine();
            if (inputStr.contains("temperature")) {
                temperature = Integer.parseInt(inputStr.replace("temperature: ", ""));
                GUI.setTemperatureLabel(temperature);
            } else if (inputStr.contains("luminosity")) {
                luminosity = Integer.parseInt(inputStr.replace("luminosity: ", ""));
                GUI.setLuminosityLabel(luminosity);
            }
        }
        br.close();
        s.close();
    }
}