package org.garden;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class GardenDashboard {
    static GardenDashboardGUI gui;
    static int luminosity;
    static int temperature;
    static int[] lights = new int[4];
    static int irrigation;
    static int mode;
    public static void main(String[] args) throws Exception {
        Socket clientSocket = new Socket("localhost", 888);
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println("Connected to server on port 888");

        // Initialize GUI
        gui = new GardenDashboardGUI();

        while(true) {
            writer.println("data");
            String line = reader.readLine();
            while (!line.isEmpty()) {
                Thread.sleep(1000);

                readData(line);
                updateGUI();

                writer.println("data");
                line = reader.readLine();
            }
        }
    }

    private static void readData(String line) {
        List<String> items = Arrays.asList(line.split("\\s*,\\s*"));
        luminosity = Integer.parseInt(items.get(0));
        temperature = Integer.parseInt(items.get(1));
        lights[0] = Integer.parseInt(items.get(2));
        lights[1] = Integer.parseInt(items.get(3));
        lights[2] = Integer.parseInt(items.get(4));
        lights[3] = Integer.parseInt(items.get(5));
        irrigation = Integer.parseInt(items.get(6));
        mode = Integer.parseInt(items.get(7));
    }

    private static void updateGUI() {
        gui.updateLuminosity(luminosity);
        gui.updateTemperature(temperature);
        gui.updateLed1(lights[0]);
        gui.updateLed2(lights[1]);
        gui.updateLed3(lights[2]);
        gui.updateLed4(lights[3]);
        gui.updateIrrigation(irrigation);
        gui.updateMode(mode);
    }
}