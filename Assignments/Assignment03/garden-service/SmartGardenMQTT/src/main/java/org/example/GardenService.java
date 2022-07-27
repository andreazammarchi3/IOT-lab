package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import java.io.*;
import java.net.*;

public class GardenService extends AbstractVerticle {

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

        String msg = "";
        // server executes continuously
        while (agent.getLuminosity() != null && agent.getTemperature() != null) {
            String luminosity = agent.getLuminosity();
            String temperature = agent.getTemperature();
            msg = luminosity + "-" + temperature;

            // send to client
            ps.println(msg);
        }
        ps.close();
        ss.close();
        s.close();

        System.exit(0);
    }

}
