package org.example;

import java.io.*;
import java.net.*;
import java.util.Objects;

public class GardenDashboard {

    private static String temperature = "0";
    private static String luminosity = "0";
    //private static final GardenDashboardGUI GUI = new GardenDashboardGUI();

    public static void main(String[] args) throws Exception {
        // Start GUI
        //GardenDashboardGUI frame = new GardenDashboardGUI();

        // Create client socket
        Socket s = new Socket("localhost", 888);

        // to read data coming from the server
        BufferedReader br
                = new BufferedReader(
                new InputStreamReader(
                        s.getInputStream()));

        String inputStr;
        while(temperature != null && luminosity != null) {
            inputStr = br.readLine();
            String[] parts = inputStr.split("-");
            luminosity = parts[0];
            temperature = parts[1];
            System.out.println("L:" + luminosity + "-T:" + temperature);
        }
        br.close();
        s.close();
    }
}