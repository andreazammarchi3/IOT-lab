package org.garden;

import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MsgTask implements Runnable {
    private Thread t;
    private final MQTTAgent agent;
    private final ServerSocket server;
    private int luminosity;
    private int temperature;
    private ServiceTask serviceTask;

    public MsgTask(ServiceTask serviceTask) throws Exception {
        // Initialize the Socket Server for Dashboard communication
        server = new ServerSocket(888);
        System.out.println("Listening for connection on port 888 ....");

        // Initialize the MQTT Agent
        Vertx vertx = Vertx.vertx();
        agent = new MQTTAgent();
        vertx.deployVerticle(agent);

        this.serviceTask = serviceTask;
    }

    @Override
    public void run() {
        while (!t.isInterrupted()) {
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
                        // getDataFromController();
                        // Get data from sensor board
                        getDataFromSensorboard();
                        serviceTask.setLuminosity(luminosity);
                        serviceTask.setTemperature(temperature);
                        // Send data to dashboard
                        writer.println(
                                luminosity + ", " +
                                        temperature + ", " +
                                        serviceTask.getLight(0) + ", " +
                                        serviceTask.getLight(0) + ", " +
                                        serviceTask.getLight(0) + ", " +
                                        serviceTask.getLight(0) + ", " +
                                        serviceTask.getIrrigation() + ", " +
                                        serviceTask.getMode());
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                t.interrupt();
            }
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, "MsgThread");
            t.start();
        }
    }

    private void getDataFromSensorboard() {
        luminosity = agent.getLuminosity();
        temperature = agent.getTemperature();
    }
}
