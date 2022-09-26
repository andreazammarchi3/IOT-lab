package org.garden;

import java.io.*;
import java.net.*;

public class GardenService {
    private static GardenSerialCommChannel gardenSerialCommChannel;
    private static int luminosity = 8;
    private static int temperature = 5;
    private static int[] lights = {1,1,2,2};
    private static int irrigation = 1;

    public static void main(String[] args) throws Exception {
        // Initialize the Socket Server
        ServerSocket server = new ServerSocket(888);
        System.out.println("Listening for connection on port 888 ....");

        // Initialize the Serial Communication with Arduino
        gardenSerialCommChannel = new GardenSerialCommChannel();

        while (true) {
            Socket clientSocket = server.accept();
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            String line = reader.readLine();
            while (!line.isEmpty()) {
                if (line.equals("data")) {
                    getDataFromController();
                    writer.println(
                            luminosity + ", " +
                            temperature + ", " +
                            lights[0] + ", " +
                            lights[1] + ", " +
                            lights[2] + ", " +
                            lights[3] + ", " +
                            irrigation);
                }
                line = reader.readLine();
            }
        }
    }

    private static void getDataFromController() {
        // luminosity = gardenSerialCommChannel.getLuminosity();
        // temperature = gardenSerialCommChannel.getTemperature();
        for (int i = 0; i < 4; i++) {
            lights[i] = gardenSerialCommChannel.getLights(i);
        }
        irrigation = gardenSerialCommChannel.getIrrigation();
    }
}