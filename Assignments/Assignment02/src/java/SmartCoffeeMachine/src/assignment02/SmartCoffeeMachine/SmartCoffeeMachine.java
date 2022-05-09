package assignment02.SmartCoffeeMachine;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SmartCoffeeMachine {
    public static void main(String[] args) throws Exception {
        String modality;
        int nCoffee;
        int nTea;
        int nChocolate;
        int nSelfTests;

        SerialCommChannel channel = new SerialCommChannel("/dev/cu.usbmodem14101", 9600);
        System.out.println("Waiting Arduino for rebooting...");
        Thread.sleep(4000);
        System.out.println("Ready.");

        channel.sendMsg("ready");
        String msg = channel.receiveMsg();
        nCoffee = Integer.parseInt(msg);
        msg = channel.receiveMsg();
        nTea = Integer.parseInt(msg);
        msg = channel.receiveMsg();
        nChocolate = Integer.parseInt(msg);

        SmartCoffeeMachineGUI frame = new SmartCoffeeMachineGUI();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.refreshGUI();

        /*
        while (true) {
            channel.sendMsg("1");
            String msg = channel.receiveMsg();
            System.out.println(msg);
            Thread.sleep(500);

            channel.sendMsg("0");
            msg = channel.receiveMsg();
            System.out.println(msg);
            Thread.sleep(500);
        }
        */
    }
}
