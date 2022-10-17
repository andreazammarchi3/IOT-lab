package org.garden;

import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GardenService {
    private static final int ACTIVITY_IRRIGATION_SECONDS = 8;
    private static final int SLEEP_IRRIGATION_SECONDS = 60;

    private static GardenSerialCommChannel controller;
    private static MQTTAgent agent;

    private static int luminosity = 0;
    private static int temperature = 0;
    private static int[] lights = new int[4];
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

        while (true) {
            Socket clientSocket = server.accept();
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            String line = reader.readLine();
            while (!line.isEmpty()) {
                // If dashboard is requesting data
                if (line.equals("data")) {
                    // Get data from sensor board
                    getDataFromSensorboard();
                    // Get data from controller
                    //getDataFromController();
                    // Send data to dashboard
                    writer.println(
                            luminosity + ", " +
                            temperature + ", " +
                            lights[0] + ", " +
                            lights[1] + ", " +
                            lights[2] + ", " +
                            lights[3] + ", " +
                            irrigation + ", " +
                            mode.getValue());
                }

                switch (mode) {
                    case AUTO -> {
                        System.out.println("AUTO");
                        if (luminosity < 5) {
                            controller.setLight(0, 1);
                            controller.setLight(1, 1);
                            controller.setLight(2, luminosity);
                            controller.setLight(3, luminosity);
                            if (sleepSecondsCounter == 0) {
                                if (activitySecondsCounter < ACTIVITY_IRRIGATION_SECONDS) {
                                    if (luminosity < 2) {
                                        irrigation = temperature;
                                        controller.setIrrigation(temperature);
                                    } else {
                                        irrigation = 0;
                                        controller.setIrrigation(0);
                                    }
                                } else {
                                    activitySecondsCounter = 0;
                                    sleepSecondsCounter++;
                                }
                            } else {
                                if (sleepSecondsCounter == SLEEP_IRRIGATION_SECONDS) {
                                    sleepSecondsCounter = 0;
                                } else {
                                    sleepSecondsCounter++;
                                }
                            }
                        } else {
                            for (int i = 0; i < 4; i++) {
                                controller.setLight(i, 0);
                            }
                        }

                        if (temperature == 5 && (activitySecondsCounter == 0 || sleepSecondsCounter != 0)) {
                            mode = Mode.ALARM;
                            controller.setMode(mode.getValue());
                            agent.setMode(mode.getValue());
                        }
                        Thread.sleep(2000);
                    }

                    case MANUAL -> {
                    }

                    case ALARM -> {
                        System.out.println("ALARM");
                    }
                }
                line = reader.readLine();
                Thread.sleep(1000);
            }
        }
    }

    /*
    private static void getDataFromController() {
        for (int i = 0; i < 4; i++) {
            lights[i] = controller.getLight(i);
        }
        irrigation = controller.getIrrigation();
    }
    */

    private static void getDataFromSensorboard() {
        luminosity = agent.getLuminosity();
        temperature = agent.getTemperature();
    }
}