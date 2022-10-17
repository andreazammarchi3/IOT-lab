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

    private static int luminosity = -1;
    private static int temperature = -1;
    private static boolean onOffLights = false;
    private static int fadeLights = -1;
    private static int irrigation = -1;
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
                switch (mode) {
                    case AUTO -> {
                        if (luminosity < 5) {
                            onOffLights = true;
                            controller.setOnOffLight(true);
                            fadeLights = luminosity;
                            controller.setFadeLights(luminosity);
                        } else {
                            onOffLights = false;
                            controller.setOnOffLight(false);
                            fadeLights = 0;
                            controller.setFadeLights(0);
                        }

                        if (temperature == 5 && (activitySecondsCounter == 0 || sleepSecondsCounter != 0)) {
                            mode = Mode.ALARM;
                            controller.setMode(mode.getValue());
                        }
                    }

                    case MANUAL -> {
                        System.out.println("MANUAL");
                    }

                    case ALARM -> {
                        System.out.println("ALARM");
                    }
                }

                line = reader.readLine();
            }
        }
    }

    private static void getDataFromSensorboard() {
        luminosity = agent.getLuminosity();
        temperature = agent.getTemperature();
    }
}