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
        String response = channel.receiveMsg();
        System.out.println(response);
        return response;
    }

    /*
    public void update() throws Exception {
        lights[0] = Integer.parseInt(getSerialData("led1"));
        lights[1] = Integer.parseInt(getSerialData("led2"));
        lights[2] = Integer.parseInt(getSerialData("led3"));
        lights[3] = Integer.parseInt(getSerialData("led4"));
        irrigation = Integer.parseInt(getSerialData("irrigation"));
        mode = Integer.parseInt(getSerialData("mode"));
    }
    */

    public int getIrrigation() {
        return irrigation;
    }

    public int getLight(int i) {
        return lights[i];
    }

    public String setIrrigation(int value) throws Exception {
        return getSerialData("irri_" + value);
    }

    public String setLight(int led, int value) throws Exception {
        return getSerialData("led" + led + "_" + value);
    }

    public String setMode(int value) throws Exception {
        switch (value) {
            case 0 -> {
                return getSerialData("AUTO");
            }
            case 1 -> {
                return getSerialData("MANUAL");
            }
            case 2 -> {
                return getSerialData("ALARM");
            }
            default -> {
                return "Error";
            }
        }
    }
}
