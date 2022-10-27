package org.garden;

import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class GardenService {
    private static final int ACTIVITY_IRRIGATION_TIME = 4;
    private static final int SLEEP_IRRIGATION_TIME = 10;

    private static GardenSerialCommChannel controller;
    private static MQTTAgent agent;

    private static int luminosity = -1;
    private static int temperature = -1;
    private static int led1 = 0;
    private static int led2 = 0;
    private static int led3 = 0;
    private static int led4 = 0;
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
                                led1 + ", " +
                                led2 + ", " +
                                led3 + ", " +
                                led4 + ", " +
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

    private static void keepIrrigating() {
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
                if (!Objects.equals(controller.getSerialData(), "empty")) {
                    if (Objects.equals(controller.getSerialData(), "MANUAL")) {
                        mode = Mode.MANUAL;
                    }
                }
                if (luminosity < 5) {
                    led1 = 1;
                    led2 = 1;
                    led3 = luminosity;
                    led4 = luminosity;
                    controller.setLed(1,1);
                    controller.setLed(1,2);
                    controller.setLed(luminosity,3);
                    controller.setLed(luminosity,4);
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
                    led1 = 0;
                    led2 = 0;
                    led3 = 0;
                    led4 = 0;
                    controller.setLed(0,1);
                    controller.setLed(0,2);
                    controller.setLed(0,3);
                    controller.setLed(0,4);
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
                if (!Objects.equals(controller.getSerialData(), "empty")) {
                    String msg = controller.getSerialData();
                    if (msg.contains("led1_")) {
                        led1 = Integer.parseInt(msg.replace("led1_", ""));
                    } else if (msg.contains("led2_")) {
                        led2 = Integer.parseInt(msg.replace("led2_", ""));
                    } else if (msg.contains("led3_")) {
                        led2 = Integer.parseInt(msg.replace("led3_", ""));
                    } else if (msg.contains("led4_")) {
                        led2 = Integer.parseInt(msg.replace("led4_", ""));
                    } else if (msg.contains("irri_")) {
                        led2 = Integer.parseInt(msg.replace("irri_", ""));
                    }
                }
            }

            case ALARM -> {
                if (Objects.equals(controller.getSerialData(), "MANUAL")) {
                    mode = Mode.MANUAL;
                }
            }
        }
    }

    public static void close() {
        System.exit(0);
    }
}