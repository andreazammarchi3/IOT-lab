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
    private static int periodCounter = 0;

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
                    mainTask();
                    // If dashboard is requesting data
                    if (line.equals("data")) {
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

    private static void keepSleeping() {
        sleepSecondsCounter++;
        if (sleepSecondsCounter == SLEEP_IRRIGATION_TIME) {
            sleepSecondsCounter = 0;
        }
    }

    private static void keepIrrigating() throws InterruptedException {
        if (irrigation != temperature) {
            irrigation = temperature;
            controller.setIrrigation(irrigation);
        }
        activitySecondsCounter++;
        if (activitySecondsCounter == ACTIVITY_IRRIGATION_TIME) {
            activitySecondsCounter = 0;
            sleepSecondsCounter++;
            irrigation = 0;
            controller.setIrrigation(0);
            keepSleeping();
        }
    }

    private static void mainTask() throws Exception {
        getDataFromSensorboard();
        getDataFromController();
        switch (mode) {
            case AUTO -> {
                if (luminosity < 5) {
                    if (led1 != 1) {
                        led1 = 1;
                        led2 = 1;
                        controller.setLed(1, 1);
                        controller.setLed(1, 2);
                    }
                    if (led3 != (5 - luminosity)) {
                        led3 = (5 - luminosity);
                        led4 = (5 - luminosity);
                        controller.setLed((5-luminosity), 3);
                        controller.setLed((5-luminosity), 4);
                    }
                    if (luminosity < 2) {
                        if (sleepSecondsCounter == 0) {
                            keepIrrigating();
                        } else {
                            keepSleeping();
                        }
                    } else {
                        if (activitySecondsCounter != 0) {
                            keepIrrigating();
                        } else if (sleepSecondsCounter != 0) {
                            keepSleeping();
                        }
                    }
                } else {
                    if (led1 != 0) {
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
                }

                if (temperature == 5 && (activitySecondsCounter == 0 || sleepSecondsCounter != 0)) {
                    mode = Mode.ALARM;
                    controller.setMode(mode.getValue());
                    agent.setMode(Mode.ALARM.getValue());
                }
            }

            case MANUAL -> {
                getDataFromSensorboard();
                getDataFromController();
            }

            case ALARM -> {
                if (controller.getMode() == Mode.MANUAL.getValue()) {
                    mode = Mode.MANUAL;
                }
            }
        }
    }

    public static void getDataFromController() throws Exception {
        String str = controller.getDataFromController();
        System.out.println(str);
        if (!Objects.equals(str, "empty")) {
            if (mode == Mode.MANUAL) {
                if (Integer.parseInt(str) != Mode.MANUAL.value) {
                    int value = Integer.parseInt(str);
                    int ones = value % 10;
                    int tens = (value / 10) % 10;
                    switch (tens) {
                        case 1 -> led1 = ones;
                        case 2 -> led2 = ones;
                        case 3 -> led3 = ones;
                        case 4 -> led4 = ones;
                        case 5 -> irrigation = ones;
                    }
                }
            } else {
                if (Integer.parseInt(str) == Mode.MANUAL.value) {
                    mode = Mode.MANUAL;
                    led1 = 0;
                    led2 = 0;
                    led3 = 0;
                    led4 = 0;
                    irrigation = 0;
                }
            }
        }
    }

    public static void close() {
        System.exit(0);
    }

    public static void setModeToManual() {
        mode = Mode.MANUAL;
    }
}