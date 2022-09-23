package org.garden;

import java.io.*;
import java.net.*;

public class GardenService {
    public static void main(String[] args) throws IOException, InterruptedException {
        // create server
        ServerSocket ss = new ServerSocket(888);

        // connect it to client socket
        Socket s = ss.accept();
        System.out.println("Connection to the client socket established");

        // to send data to the client
        PrintStream ps = new PrintStream(s.getOutputStream());

        int l = 8;
        int t = 5;
        boolean even = false;
        String msg = "";

        while(s.isConnected()) {
            long millis = System.currentTimeMillis();

            try {
                if (even) {
                    msg = "l: " + l;
                    even = false;
                    l++;
                } else {
                    even = true;
                    msg = "t: " + t;
                    t++;
                }
                ps.println(msg);
            } catch (Exception e) {
                System.out.println("ERROR");
                ps.println("ERROR");
            }

            Thread.sleep(1000 - millis % 1000);
        }

        ps.close();
        ss.close();
        s.close();
        System.out.println("Server and client CLOSED");
    }
}