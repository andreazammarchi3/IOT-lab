package org.garden;

public class GardenService {
    private static MsgTask msgTask;
    private static ServiceTask serviceTask;
    private volatile int luminosity = 8;
    private volatile int temperature = 5;
    private volatile int[] lights = {1,1,2,2};
    private volatile int irrigation = 1;
    private volatile int mode = 0;

    public static void main(String[] args) throws Exception {
        msgTask = new MsgTask();
        serviceTask = new ServiceTask();

    }
}