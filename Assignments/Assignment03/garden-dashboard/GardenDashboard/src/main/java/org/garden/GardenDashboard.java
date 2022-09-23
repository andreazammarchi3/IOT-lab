package org.garden;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

public class GardenDashboard {
    public static void main(String[] args) throws Exception {
        // Initialize client socket
        Socket s = new Socket("localhost", 888);

        // to read data coming from the server
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        // Initialize GUI
        GardenDashboardGUI gui = new GardenDashboardGUI();

        String inputStr = "";
        while(!Objects.equals(inputStr, "close")) {
            long millis = System.currentTimeMillis();

            inputStr = br.readLine();
            if (inputStr.contains("l")) {
                int l = Integer.parseInt(inputStr.replace("l: ",""));
                gui.setValueL(l);
            } else {
                int t = Integer.parseInt(inputStr.replace("t: ",""));
                gui.setValueT(t);
            }

            Thread.sleep(1000 - millis % 1000);
        }

        br.close();
        s.close();
    }
}