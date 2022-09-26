package org.garden;

import java.io.*;
import java.net.*;

public class GardenService {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(888);
        System.out.println("Listening for connection on port 888 ....");

        int l = 8;
        int t = 5;
        while (true) {
            Socket clientSocket = server.accept();
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String line = reader.readLine();
            while (!line.isEmpty()) {
                if (line.equals("l, t")) {
                    System.out.println(line);
                    writer.println(l + ", " + t);
                }
                line = reader.readLine();
            }
        }

    }
}