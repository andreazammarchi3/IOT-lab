package org.garden;

public class GardenSerialCommChannel {
    /**
     * Serial Communication Channel
     */
    private SerialCommChannel channel;

    public GardenSerialCommChannel() throws Exception {
        channel = new SerialCommChannel("/dev/cu.usbmodem14101", 9600);
        System.out.println("Waiting Arduino for rebooting...");
        Thread.sleep(2000);
        initialize();
        System.out.println("Ready.");
    }

    public void initialize() throws Exception {

    }

    public String getSerialData(String inputMsg) throws Exception {
        channel.sendMsg(inputMsg);
        return channel.receiveMsg();
    }

    public void sendSerialData(String inputMsg) {
        channel.sendMsg(inputMsg);
    }

    public void update() throws Exception {

    }
}
