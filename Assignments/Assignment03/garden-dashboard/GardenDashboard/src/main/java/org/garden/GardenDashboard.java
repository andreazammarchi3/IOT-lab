package org.garden;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class GardenDashboard {
    public static void main(String[] args) throws Exception {
        Socket clientSocket = new Socket("localhost", 888);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println("Connected to server on port 888");

        // Initialize GUI
        GardenDashboardGUI gui = new GardenDashboardGUI();

        int l = 0;
        int t = 0;
        while(true) {
            writer.println("l, t");
            String line = reader.readLine();
            while (!line.isEmpty()) {
                Thread.sleep(1000);
                List<String> items = Arrays.asList(line.split("\\s*,\\s*"));
                l = Integer.parseInt(items.get(0));
                t = Integer.parseInt(items.get(1));
                gui.setValueL(l);
                gui.setValueT(t);
                System.out.println("Luminosity: " + l + ", Temperature: " + t);
                writer.println("l, t");
                line = reader.readLine();
            }
        }
    }
}