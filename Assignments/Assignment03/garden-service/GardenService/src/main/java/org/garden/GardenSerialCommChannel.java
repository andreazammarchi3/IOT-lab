package org.garden;

import org.garden.channel.ExtendedSerialCommChannel;

import java.util.Objects;

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

    public void sendSerialData(String inputMsg) throws InterruptedException {
        channel.sendMsg(inputMsg);
        channel.receiveMsg();
    }

    public String getSerialData() throws InterruptedException {
        if (channel.isMsgAvailable()) {
            return channel.receiveMsg();
        } else {
            return "empty";
        }
    }

    public void setIrrigation(int value) throws InterruptedException {
        sendSerialData("irri_" + value);
    }

    public void setLed(int value, int led) throws InterruptedException {
        sendSerialData("led" + led + "_" + value);
    }

    public void setMode(int value) throws InterruptedException {
        switch (value) {
            case 0 -> sendSerialData("AUTO");
            case 1 -> sendSerialData("MANUAL");
            case 2 -> sendSerialData("ALARM");
        }
    }

    public int getIrrigation() throws Exception {
        sendSerialData("irri");
        String value = getSerialData();
        if (!Objects.equals(value, "empty")) {
            return Integer.parseInt(value);
        } else {
            return -1;
        }
    }

    public int getLed(int led) throws Exception {
        sendSerialData("led" + led);
        String value = getSerialData();
        if (!Objects.equals(value, "empty")) {
            return Integer.parseInt(value);
        } else {
            return -1;
        }
    }

    public int getMode() throws Exception {
        sendSerialData("mode");
        String value = getSerialData();
        if (!Objects.equals(value, "empty")) {
            return Integer.parseInt(value);
        } else {
            return -1;
        }
    }

    public void close() throws InterruptedException {
        sendSerialData("CLOSE");
        channel.close();
    }
}
