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

    private static void mainTask() throws Exception {
        getDataFromSensorboard();
        switch (mode) {
            case AUTO -> {
                if (controller.getMode() == Mode.MANUAL.getValue()) {
                    mode = Mode.MANUAL;
                }

                if (luminosity < 5) {
                    led1 = 1;
                    led2 = 1;
                    led3 = (5-luminosity);
                    led4 = (5-luminosity);
                    controller.setLed(1, 1);
                    controller.setLed(1, 2);
                    controller.setLed((5-luminosity), 3);
                    controller.setLed((5-luminosity), 4);

                    if (luminosity < 2) {
                        irrigation = temperature;
                        controller.setIrrigation(irrigation);
                    } else {
                        irrigation = 0;
                        controller.setIrrigation(irrigation);
                    }
                } else {
                    led1 = 0;
                    led2 = 0;
                    led3 = 0;
                    led4 = 0;
                    controller.setLed(0, 1);
                    controller.setLed(0, 2);
                    controller.setLed(0, 3);
                    controller.setLed(0, 4);
                    irrigation = 0;
                    controller.setIrrigation(irrigation);
                }

                if (temperature == 5 && (activitySecondsCounter == 0 || sleepSecondsCounter != 0)) {
                    mode = Mode.ALARM;
                    controller.setMode(mode.getValue());
                    agent.setMode(Mode.ALARM.getValue());
                }
            }

            case MANUAL -> getDataFromController();

            case ALARM -> {
                if (controller.getMode() == Mode.MANUAL.getValue()) {
                    mode = Mode.MANUAL;
                }
            }
        }
    }

    public static void getDataFromController() throws Exception {
        led1 = controller.getLed(1);
        led2 = controller.getLed(2);
        led3 = controller.getLed(3);
        led4 = controller.getLed(4);
        irrigation = controller.getIrrigation();
    }

    public static void close() {
        System.exit(0);
    }
}