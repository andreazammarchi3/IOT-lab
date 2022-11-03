package org.garden;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class GardenDashboard {
    private static GardenDashboardGUI gui;

    private static Socket clientSocket;
    private static BufferedReader reader;
    private static PrintWriter writer;

    private static int luminosity;
    private static int temperature;
    private static int led1;
    private static int led2;
    private static int led3;
    private static int led4;
    private static int irrigation;
    private static int mode;
    public static void main(String[] args) throws Exception {
        clientSocket = new Socket("localhost", 888);
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println("Connected to server on port 888");

        // Initialize GUI
        gui = new GardenDashboardGUI();

        while(true) {
            try {
                writer.println("data");
                String line = reader.readLine();
                while (!line.isEmpty()) {
                    Thread.sleep(1000);

                    readData(line);
                    updateGUI();

                    writer.println("data");
                    line = reader.readLine();
                }
                close();
            } catch (Exception e) {
                close();
            }
        }
    }

    private static void readData(String line) {
        List<String> items = Arrays.asList(line.split("\\s*,\\s*"));
        luminosity = Integer.parseInt(items.get(0));
        temperature = Integer.parseInt(items.get(1));
        led1 = Integer.parseInt(items.get(2));
        led2 = Integer.parseInt(items.get(3));
        led3 = Integer.parseInt(items.get(4));
        led4 = Integer.parseInt(items.get(5));
        irrigation = Integer.parseInt(items.get(6));
        mode = Integer.parseInt(items.get(7));
    }

    private static void updateGUI() {
        gui.updateLuminosity(luminosity);
        gui.updateTemperature(temperature);
        gui.updateLed(led1, 1);
        gui.updateLed(led2, 2);
        gui.updateLed(led3, 3);
        gui.updateLed(led4, 4);
        gui.updateIrrigation(irrigation);
        gui.updateMode(mode);
    }

    public static void close() throws IOException {
        clientSocket.close();
        reader.close();
        writer.close();
        System.exit(0);
    }
}