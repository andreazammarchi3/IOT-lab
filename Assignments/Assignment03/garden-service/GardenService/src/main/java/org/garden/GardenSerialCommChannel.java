package org.garden;

import org.garden.channel.ExtendedSerialCommChannel;

public class GardenSerialCommChannel {
    /**
     * Serial Communication Channel
     */
    private final ExtendedSerialCommChannel channel;

    public GardenSerialCommChannel() throws Exception {
        channel = new ExtendedSerialCommChannel("/dev/cu.usbmodem14201", 9600, 8080);
        System.out.println("Waiting Arduino for rebooting...");
        Thread.sleep(2000);
        System.out.println("Ready.");
    }

    public void sendSerialData(String inputMsg) {
        channel.sendMsg(inputMsg);
    }

    public String getSerialData() throws InterruptedException {
        if (channel.isMsgAvailable()) {
            return channel.receiveMsg();
        } else {
            return "empty";
        }
    }

    public void setIrrigation(int value) {
        sendSerialData("irri_" + value);
    }

    public void setLed(int value, int led) {
        sendSerialData("led" + led + "_" + value);
    }

    public void setMode(int value) {
        switch (value) {
            case 0 -> sendSerialData("AUTO");
            case 1 -> sendSerialData("MANUAL");
            case 2 -> sendSerialData("ALARM");
        }
    }

    public void close() {
        sendSerialData("CLOSE");
        channel.close();
    }
}
