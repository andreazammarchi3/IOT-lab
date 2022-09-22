package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class GardenDashboard {


    public static void main(String[] args) throws Exception {
        // Create client socket
        Socket s = new Socket("localhost", 888);

        // to read data coming from the server

        BufferedReader br
                = new BufferedReader(
                new InputStreamReader(
                        s.getInputStream()));

        String inputStr = br.readLine();
        System.out.println(inputStr);

        br.close();
        s.close();
    }
}