package org.garden;

import org.garden.channel.ExtendedSerialCommChannel;

import java.util.Objects;

public class GardenSerialCommChannel {
    /**
     * Serial Communication Channel
     */
    private final ExtendedSerialCommChannel channel;

    public GardenSerialCommChannel() throws Exception {
        channel = new ExtendedSerialCommChannel("/dev/cu.usbmodem14201", 9600, 880);
        System.out.println("Waiting Arduino for rebooting...");
        Thread.sleep(2000);
        System.out.println("Ready.");
    }

    public String sendSerialData(String inputMsg) throws InterruptedException {
        channel.sendMsg(inputMsg);
        System.out.println("Sent: " + inputMsg);
        String msg = channel.receiveMsg();
        System.out.println("Received: " + msg);
        return msg;
    }

    public void setIrrigation(int value) throws InterruptedException {
        sendSerialData("5" + value);
    }

    public void setLed(int value, int led) throws InterruptedException {
        sendSerialData(String.valueOf(led) + String.valueOf(value));
    }

    public void setMode(int value) throws InterruptedException {
        switch (value) {
            case 0 -> sendSerialData("AUTO");
            case 1 -> sendSerialData("MANUAL");
            case 2 -> sendSerialData("ALARM");
        }
    }

    public int getIrrigation() throws Exception {
        String value = sendSerialData("irri");
        if (!Objects.equals(value, "empty")) {
            return Integer.parseInt(value);
        } else {
            return -1;
        }
    }

    public int getLed(int led) throws Exception {
        String value = sendSerialData("led" + led);
        if (!Objects.equals(value, "empty")) {
            return Integer.parseInt(value);
        } else {
            return -1;
        }
    }

    public int getMode() throws Exception {
        String value = sendSerialData("mode");
        return Integer.parseInt(value);
    }

    public void close() throws InterruptedException {
        sendSerialData("CLOSE");
        channel.close();
    }

    public String getDataFromController() {
        if (channel.isMsgAvailable()) {
            try {
                return channel.receiveMsg();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            return "empty";
        }
    }
}
