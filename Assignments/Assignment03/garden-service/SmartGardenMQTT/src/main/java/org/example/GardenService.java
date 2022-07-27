package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import java.io.*;
import java.net.*;

public class GardenService extends AbstractVerticle {

    private static int temperature = 0;
    private static int luminosity = 0;

    public static void main(String[] args) throws Exception {
        Vertx vertx = Vertx.vertx();
        MQTTAgent agent = new MQTTAgent();
        vertx.deployVerticle(agent);

        // Server socket
        // create server
        ServerSocket ss = new ServerSocket(888);

        // connect it to client socket
        Socket s = ss.accept();
        System.out.println("Connection to the client socket established");

        // to send data to the client
        PrintStream ps = new PrintStream(s.getOutputStream());

        // server executes continuously
        while (agent.getLuminosity() != -1 && agent.getTemperature() != -1) {
            // check if values changed
            if (agent.getLuminosity() != luminosity || agent.getTemperature() != temperature) {
                luminosity = agent.getLuminosity();
                temperature = agent.getTemperature();
                String tempMsg = "temperature: " + temperature;
                String lumMsg = "luminosity: " + luminosity;

                // send to client
                ps.println(tempMsg);
                ps.println(lumMsg);
            }

        }
        ps.close();
        ss.close();
        s.close();

        System.exit(0);
    }

}
