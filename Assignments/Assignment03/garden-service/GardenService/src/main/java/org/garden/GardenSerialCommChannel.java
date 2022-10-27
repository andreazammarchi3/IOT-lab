package org.garden;

import org.garden.channel.CommChannel;
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

    public void sendSerialData(String inputMsg) throws Exception {
        channel.sendMsg(inputMsg);
        // channel.receiveMsg();
    }

    public void setIrrigation(int value) throws Exception {
        sendSerialData("irri_" + value);
    }

    public void setOnOffLight(boolean value) throws Exception {
        String msg;
        if (value) {
            msg = "leds_" + 1;
        } else {
            msg = "leds_" + 0;
        }
        sendSerialData(msg);
    }

    public void setFadeLights(int value) throws Exception {
        sendSerialData("fade_" + value);
    }

    public void setMode(int value) throws Exception {
        switch (value) {
            case 0 -> {
                sendSerialData("AUTO");
            }
            case 1 -> {
                sendSerialData("MANUAL");
            }
            case 2 -> {
                sendSerialData("ALARM");
            }
        }
    }

    public void close() throws Exception {
        sendSerialData("CLOSE");
        channel.close();
    }
}
