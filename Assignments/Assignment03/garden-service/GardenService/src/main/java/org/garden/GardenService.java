package org.garden;

public class GardenService {

    public static void main(String[] args) throws Exception {
        ServiceTask serviceTask = new ServiceTask();
        MsgTask msgTask = new MsgTask(serviceTask);

        serviceTask.start();
        msgTask.start();
    }
}