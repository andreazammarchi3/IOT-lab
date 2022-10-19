package org.garden;

import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GardenService {
    private static final int ACTIVITY_IRRIGATION_TIME = 4;
    private static final int SLEEP_IRRIGATION_TIME = 10;

    private static GardenSerialCommChannel controller;
    private static MQTTAgent agent;

    private static int luminosity = -1;
    private static int temperature = -1;
    private static boolean onOffLights = false;
    private static int fadeLights = -1;
    private static int irrigation = 0;
    private static Mode mode = Mode.AUTO;
    private static int activitySecondsCounter = 0;
    private static int sleepSecondsCounter = 0;

    private enum Mode {
        AUTO(0),
        MANUAL(1),
        ALARM(2);

        private final int value;

        Mode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static void main(String[] args) throws Exception {
        // Initialize the Socket Server
        ServerSocket server = new ServerSocket(888);
        System.out.println("Listening for connection on port 888 ....");

        // Initialize the Serial Communication with Arduino
        controller = new GardenSerialCommChannel();

        // Initialize the MQTT Agent
        Vertx vertx = Vertx.vertx();
        agent = new MQTTAgent();
        vertx.deployVerticle(agent);

        // Initialize the GUI
        GardenServiceGUI gui = new GardenServiceGUI();

        while (true) {
            try {
                Socket clientSocket = server.accept();
                InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                String line = reader.readLine();
                while (!line.isEmpty()) {
                    // If dashboard is requesting data
                    if (line.equals("data")) {
                        getDataFromSensorboard();
                        // Send data to dashboard
                        writer.println(
                                luminosity + ", " +
                                temperature + ", " +
                                onOffLights + ", " +
                                fadeLights + ", " +
                                irrigation + ", " +
                                mode.getValue()
                        );
                    }
                    mainTask();
                    line = reader.readLine();
                }
                close();
            } catch (Exception e) {
                close();
            }
        }
    }

    private static void getDataFromSensorboard() {
        // Get data from sensor board
        luminosity = agent.getLuminosity();
        temperature = agent.getTemperature();
    }

    private static void keepIrrigating() throws Exception {
        irrigation = temperature;
        controller.setIrrigation(temperature);
        activitySecondsCounter++;
        if (activitySecondsCounter == ACTIVITY_IRRIGATION_TIME) {
            activitySecondsCounter = 0;
            sleepSecondsCounter++;
            irrigation = 0;
            controller.setIrrigation(0);
            keepSleeping();
        }
    }

    private static void keepSleeping() {
        sleepSecondsCounter++;
        if (sleepSecondsCounter == SLEEP_IRRIGATION_TIME) {
            sleepSecondsCounter = 0;
        }
    }

    private static void mainTask() throws Exception {
        getDataFromSensorboard();
        switch (mode) {
            case AUTO -> {
                if (luminosity < 5) {
                    onOffLights = true;
                    controller.setOnOffLight(true);
                    fadeLights = luminosity;
                    controller.setFadeLights(luminosity);
                    if (luminosity < 2) {
                        if (sleepSecondsCounter == 0) {
                            keepIrrigating();
                        } else {
                            keepSleeping();
                        }
                    } else {
                        if (activitySecondsCounter != 0) {
                            keepIrrigating();
                        }
                        else if (sleepSecondsCounter != 0) {
                            keepSleeping();
                        }
                    }
                } else {
                    onOffLights = false;
                    controller.setOnOffLight(false);
                    fadeLights = 0;
                    controller.setFadeLights(0);
                }

                if (temperature == 5 && (activitySecondsCounter == 0 || sleepSecondsCounter != 0)) {
                    mode = Mode.ALARM;
                    controller.setMode(mode.getValue());
                    agent.setMode(Mode.ALARM.getValue());
                } else {
                    agent.setMode(Mode.AUTO.getValue());
                }
            }

            case MANUAL -> {
                System.out.println("MANUAL");
            }

            case ALARM -> {
                System.out.println("ALARM");
            }
        }
    }

    public static void close() throws Exception {
        System.exit(0);
    }
}