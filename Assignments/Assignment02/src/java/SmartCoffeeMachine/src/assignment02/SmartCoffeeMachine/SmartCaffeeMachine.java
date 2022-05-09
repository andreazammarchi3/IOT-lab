package assignment02.SmartCoffeeMachine;

import jssc.*;

public class SmartCaffeeMachine {
    public static void main(String[] args) throws Exception {
        SerialCommChannel channel = new SerialCommChannel(args[0], 9600);
        System.out.println("Waiting Arduino for rebooting...");
        Thread.sleep(4000);
        System.out.println("Ready.");

        while (true) {
            channel.sendMsg("1");
            String msg = channel.receiveMsg();
            System.out.println(msg);
            Thread.sleep(500);

            channel.sendMsg("1");
            msg = channel.receiveMsg();
            System.out.println(msg);
            Thread.sleep(500);
        }
    }
}
