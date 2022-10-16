package org.garden;

public class GardenSerialCommChannel {
    /**
     * Serial Communication Channel
     */
    private final SerialCommChannel channel;
    private int[] lights = new int[4];
    private int irrigation = 0;
    private int mode = 0;

    public GardenSerialCommChannel() throws Exception {
        channel = new SerialCommChannel("/dev/cu.usbmodem14101", 9600);
        System.out.println("Waiting Arduino for rebooting...");
        Thread.sleep(2000);
        //update();
        System.out.println("Ready.");
    }

    public String getSerialData(String inputMsg) throws Exception {
        channel.sendMsg(inputMsg);
        return channel.receiveMsg();
    }

    public void sendSerialData(String inputMsg) {
        channel.sendMsg(inputMsg);
    }

    public void update() throws Exception {
        lights[0] = Integer.parseInt(getSerialData("led1"));
        lights[1] = Integer.parseInt(getSerialData("led2"));
        lights[2] = Integer.parseInt(getSerialData("led3"));
        lights[3] = Integer.parseInt(getSerialData("led4"));
        irrigation = Integer.parseInt(getSerialData("irrigation"));
        mode = Integer.parseInt(getSerialData("mode"));
    }

    public void setIrrigation(int value) {
        sendSerialData("irri_" + value);
    }

    public void setLight(int led, int value) {
        sendSerialData("led" + led + "_" + value);
    }

    public void setMode(int value) {
        switch (value) {
            case 0 -> sendSerialData("AUTO");
            case 1 -> sendSerialData("MANUAL");
            case 2 -> sendSerialData("ALARM");
        }
    }

    public int getIrrigation() {
        return irrigation;
    }

    public int getLight(int led) {
        return lights[led];
    }
}
