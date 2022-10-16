package org.garden;

public class ServiceTask implements Runnable {
    private Thread t;
    GardenSerialCommChannel controller = new GardenSerialCommChannel();
    private int luminosity = 0;
    private int temperature = 0;
    private Mode mode = Mode.AUTO;
    private int activitySecondsCounter = 0;
    private int sleepSecondsCounter = 0;
    private static final int ACTIVITY_IRRIGATION_SECONDS = 8;
    private static final int SLEEP_IRRIGATION_SECONDS = 60;

    public ServiceTask() throws Exception {
    }

    private enum Mode {
        AUTO(0),
        MANUAL(1),
        ALARM(2);

        private final int value;

        Mode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
    @Override
    public void run() {
        while (!t.isInterrupted()) {
            try {
                switch (mode) {
                    case AUTO:
                        if (luminosity < 5) {
                            controller.setLight(0, 1);
                            controller.setLight(1, 1);
                            controller.setLight(2, luminosity);
                            controller.setLight(3, luminosity);
                            if (sleepSecondsCounter == 0) {
                                if (activitySecondsCounter < ACTIVITY_IRRIGATION_SECONDS) {
                                    if (luminosity < 2) {
                                        controller.setIrrigation(temperature);
                                    } else {
                                        controller.setIrrigation(0);
                                    }
                                } else {
                                    activitySecondsCounter = 0;
                                    sleepSecondsCounter++;
                                }
                            } else {
                                if (sleepSecondsCounter == SLEEP_IRRIGATION_SECONDS) {
                                    sleepSecondsCounter = 0;
                                } else {
                                    sleepSecondsCounter++;
                                }
                            }
                        } else {
                            for (int i = 0; i < 4; i++) {
                                controller.setLight(i, 0);
                            }
                        }

                        if (temperature == 5 && (activitySecondsCounter == 0 || sleepSecondsCounter != 0)) {
                            mode = Mode.ALARM;
                            controller.setMode(mode.getValue());
                        }
                        // wait(1000);
                        break;

                    case MANUAL:
                        break;

                    case ALARM:
                        System.out.println("ALARM");
                        break;
                }
            } catch (Exception e) {
                t.interrupt();
            }
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, "ServiceThread");
            t.start();
        }
    }

    public void setLuminosity(int luminosity) {
        this.luminosity = luminosity;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getIrrigation() {
        return controller.getIrrigation();
    }

    public int getLight(int led) {
        return controller.getLight(led);
    }

    public int getMode() {
        return mode.getValue();
    }
}
