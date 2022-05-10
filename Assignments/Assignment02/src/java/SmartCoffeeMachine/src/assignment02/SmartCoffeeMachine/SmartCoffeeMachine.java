package assignment02.SmartCoffeeMachine;

import javax.swing.*;

public class SmartCoffeeMachine {
    public static void main(String[] args) throws Exception {

        SmartCoffeeMachineTracker tracker = new SmartCoffeeMachineTracker();

        SmartCoffeeMachineGUI frame = new SmartCoffeeMachineGUI(tracker);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        while (true) {
            tracker.update();
            frame.refreshGUI();
            Thread.sleep(100);
        }
    }
}
