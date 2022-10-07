package org.garden;

import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GardenService {
    private static GardenSerialCommChannel gardenSerialCommChannel;
    private static MQTTAgent agent;
    private static int luminosity = 8;
    private static int temperature = 5;
    private static int[] lights = {1,1,2,2};
    private static int irrigation = 1;
    private static int mode = 0;

    public static void main(String[] args) throws Exception {
        // Initialize the Socket Server
        ServerSocket server = new ServerSocket(888);
        System.out.println("Listening for connection on port 888 ....");

        // Initialize the Serial Communication with Arduino
        gardenSerialCommChannel = new GardenSerialCommChannel();

        // Initialize the MQTT Agent
        Vertx vertx = Vertx.vertx();
        agent = new MQTTAgent();
        vertx.deployVerticle(agent);

        while (true) {
            Socket clientSocket = server.accept();
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            String line = reader.readLine();
            while (!line.isEmpty()) {
                // If dashboard is requesting data
                if (line.equals("data")) {
                    // Get data from controller
                    getDataFromController();
                    // Get data from sensor board
                    getDataFromSensorboard();
                    // Send data to dashboard
                    writer.println(
                            luminosity + ", " +
                            temperature + ", " +
                            lights[0] + ", " +
                            lights[1] + ", " +
                            lights[2] + ", " +
                            lights[3] + ", " +
                            irrigation + ", " +
                            mode);
                }
                line = reader.readLine();
            }
        }
    }

    private static void getDataFromController() {
        for (int i = 0; i < 4; i++) {
            lights[i] = gardenSerialCommChannel.getLights(i);
        }
        irrigation = gardenSerialCommChannel.getIrrigation();
        mode = gardenSerialCommChannel.getMode();
    }

    private static void getDataFromSensorboard() {
        luminosity = agent.getLuminosity();
        temperature = agent.getTemperature();
    }
}