package org.garden;

import java.io.*;
import java.net.*;

public class GardenService {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(888);
        System.out.println("Listening for connection on port 888 ....");

        int l = 8;
        int t = 5;
        int[] lights = {1,1,2,2};
        int irrigation = 1;
        while (true) {
            Socket clientSocket = server.accept();
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String line = reader.readLine();
            while (!line.isEmpty()) {
                if (line.equals("data")) {
                    writer.println(
                            l + ", " +
                            t + ", " +
                            lights[0] + ", " +
                            lights[1] + ", " +
                            lights[2] + ", " +
                            lights[3] + ", " +
                            irrigation);
                }
                line = reader.readLine();
            }
        }

    }
}