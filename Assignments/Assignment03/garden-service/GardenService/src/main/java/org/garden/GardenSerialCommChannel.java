package org.garden;

public class GardenSerialCommChannel {
    /**
     * Serial Communication Channel
     */
    private final SerialCommChannel channel;

    public GardenSerialCommChannel() throws Exception {
        channel = new SerialCommChannel("/dev/cu.usbmodem14101", 9600);
        System.out.println("Waiting Arduino for rebooting...");
        Thread.sleep(2000);
        //update();
        System.out.println("Ready.");
    }

    public void getSerialData(String inputMsg) throws Exception {
        channel.sendMsg(inputMsg);
        channel.receiveMsg();
    }


    public void setIrrigation(int value) throws Exception {
        getSerialData("irri_" + value);
    }

    public void setOnOffLight(boolean value) throws Exception {
        String msg;
        if (value) {
            msg = "leds_" + 1;
        } else {
            msg = "leds_" + 0;
        }
        getSerialData(msg);
    }

    public void setFadeLights(int value) throws Exception {
        getSerialData("fade_" + value);
    }

    public void setMode(int value) throws Exception {
        switch (value) {
            case 0 -> {
                getSerialData("AUTO");
            }
            case 1 -> {
                getSerialData("MANUAL");
            }
            case 2 -> {
                getSerialData("ALARM");
            }
        }
    }

    public void close() throws Exception {
        getSerialData("CLOSE");
        channel.close();
    }
}
