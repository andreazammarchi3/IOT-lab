package org.garden;

import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MsgTask implements Runnable{
    private final GardenSerialCommChannel gardenSerialCommChannel;
    private final MQTTAgent agent;
    private final ServerSocket server;
    private int luminosity;
    private int temperature;
    private int[] lights = new int[4];
    private int irrigation;
    private int mode;
    private boolean running = true;

    public MsgTask() throws Exception {
        // Initialize the Serial Communication with Arduino
        gardenSerialCommChannel = new GardenSerialCommChannel();

        // Initialize the Socket Server for Dashboard communication
        server = new ServerSocket(888);
        System.out.println("Listening for connection on port 888 ....");

        // Initialize the MQTT Agent
        Vertx vertx = Vertx.vertx();
        agent = new MQTTAgent();
        vertx.deployVerticle(agent);
    }

    @Override
    public void run() {
        while (isRunning()) {
            try {
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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getDataFromController() {
        for (int i = 0; i < 4; i++) {
            lights[i] = gardenSerialCommChannel.getLights(i);
        }
        irrigation = gardenSerialCommChannel.getIrrigation();
        mode = gardenSerialCommChannel.getMode();
    }

    private void getDataFromSensorboard() {
        luminosity = agent.getLuminosity();
        temperature = agent.getTemperature();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean state) {
        running = state;
    }

    public void setLuminosity(int luminosity) {
        this.luminosity = luminosity;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setLights(int position, int value) {
        this.lights[position] = value;
    }

    public void setIrrigation(int irrigation) {
        this.irrigation = irrigation;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
