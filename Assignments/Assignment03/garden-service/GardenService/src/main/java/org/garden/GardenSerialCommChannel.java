package org.garden;

public class GardenSerialCommChannel {
    /**
     * Serial Communication Channel
     */
    private SerialCommChannel channel;
    private int[] lights = {0,0,0,0};
    private int irrigation = 0;
    private int mode = 0;

    public GardenSerialCommChannel() throws Exception {
        channel = new SerialCommChannel("/dev/cu.usbmodem14101", 9600);
        System.out.println("Waiting Arduino for rebooting...");
        Thread.sleep(2000);
        update();
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

    public int getIrrigation() {
        return irrigation;
    }

    public int getLights(int i) {
        return lights[i];
    }

    public int getMode() { return mode; }
}
